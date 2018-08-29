package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment;

import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentdriveEventType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.EdgeUpdateMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.PlanCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.SimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.RoadNetworkRouter;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.PlansOut;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import org.apache.log4j.Logger;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * public class {@link HighwayEnvironment} provides {@link HighwayStorage},
 * {@link RoadNetwork}
 */
@SuppressWarnings("JavadocReference")
public class HighwayEnvironment {

    private final Logger logger = Logger.getLogger(HighwayEnvironment.class);

    private HighwayStorage storage;
    private RoadNetwork roadNetwork;
    private List<SimulatorHandler> simulatorHandlers = new LinkedList<SimulatorHandler>();
    private EventProcessor ep;
    PlanCallback planCallback;

    public HighwayEnvironment() {

    }

    public HighwayEnvironment(final EventProcessor eventProcessor) {
        ep = eventProcessor;
//        XMLReader xmlReader = new XMLReader();
//        roadNetwork = xmlReader.parseNetwork(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
//        RoadNetworkRouter.setRoadNet(roadNetwork);
        storage = new HighwayStorage(this);
        planCallback = new PlanCallbackImp();
        logger.info("Initialized storage and RoadNetwork");
    }

    public HighwayEnvironment(final EventProcessor eventProcessor, RoadNetwork roadNetwork) {
        ep = eventProcessor;
        this.roadNetwork = roadNetwork;
        RoadNetworkRouter.setRoadNet(roadNetwork);
        storage = new HighwayStorage(this);
        planCallback = new PlanCallbackImp();
        logger.info("Initialized storage and RoadNetwork");
    }

    public void updateCars(RadarData radarData) {
        getStorage().updateCars(radarData);
    }

    public Map<Integer, Agent> getAgents() {
        return getStorage().getAgents();
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public void setRoadNetwork(RoadNetwork roadNetwork) {
        this.roadNetwork = roadNetwork;
        RoadNetworkRouter.setRoadNet(roadNetwork);
    }

    public HighwayStorage getStorage() {
        return storage;
    }

    public void addSimulatorHandler(SimulatorHandler sim) {
        simulatorHandlers.add(sim);
    }

    public List<SimulatorHandler> getSimulatorHandlers() {
        return simulatorHandlers;
    }

    public long getCurrentTime() {
        return ep == null ? 0 : ep.getCurrentTime();
    }

    public PlanCallback getPlanCallback() {
        return planCallback;
    }

    class PlanCallbackImp extends PlanCallback {
        @Override
        public RadarData execute(PlansOut plans) {
            Map<Integer, RoadObject> currStates = plans.getCurrStates();
            RadarData radarData = new RadarData();
            float duration = 0;
            float lastDuration = 0;
            double timest = Configurator.getParamDouble("highway.SimulatorLocal.timestep", 1.0);
            float timestep = (float) timest;

            boolean removeCar = false;
            for (Integer carID : plans.getCarIds()) {
                Collection<Action> plan = plans.getPlan(carID);
                RoadObject state = currStates.get(carID);
                Point3f lastPosition = state.getPosition();
                String previousEdgeId = roadNetwork.getActualPosition(lastPosition).getEdge().getId();
                Point3f myPosition = state.getPosition();
                for (Action action : plan) {
                    if (action.getClass().equals(WPAction.class)) {
                        WPAction wpAction = (WPAction) action;
                        if (wpAction.getSpeed() == -1) {
                            myPosition = wpAction.getPosition();
                            removeCar = true;
                        }
                        if (wpAction.getSpeed() < 0.001) {
                            duration += 0.10f;
                        } else {
                            myPosition = wpAction.getPosition();
                            lastDuration = (float) (wpAction.getPosition().distance(lastPosition) / (wpAction.getSpeed()));
                            duration += wpAction.getPosition().distance(lastPosition) / (wpAction.getSpeed());
                        }
                        // creating point between the waypoints if my duration is greater than the defined timestep
                        if (duration >= timestep) {
                            float remainingDuration = timestep - (duration - lastDuration);
                            float ration = remainingDuration / lastDuration;
                            float x = myPosition.x - lastPosition.x;
                            float y = myPosition.y - lastPosition.y;
                            float z = myPosition.z - lastPosition.z;
                            Vector3f vec = new Vector3f(x, y, z);
                            vec.scale(ration);

                            myPosition = new Point3f(vec.x + 0 + lastPosition.x, vec.y + lastPosition.y, vec.z + lastPosition.z);
                            break;
                        }
                        lastPosition = wpAction.getPosition();
                    }
                }
                if (removeCar) {
                    if (Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
                        this.addToPlannedVehiclesToRemove(carID);
                    }
                    removeCar = false;
                } else {
                    Vector3f vel = new Vector3f(state.getPosition());
                    vel.negate();
                    vel.add(myPosition);
                    if (vel.length() < 0.0001) {
                        vel = state.getVelocity();
                        vel.normalize();
                        vel.scale(0.0010f);
                    }
                    int lane = roadNetwork.getClosestLane(myPosition).getIndex();
                    state = new RoadObject(carID, getCurrentTime(), lane, myPosition, vel);
                    radarData.add(state);
                    duration = 0;
                    if (!previousEdgeId.equals(roadNetwork.getActualPosition(state.getPosition()).getEdge().getId())) {
                        ep.addEvent(AgentdriveEventType.UPDATE_EDGE, null, null, new EdgeUpdateMessage(carID));
                    }
                }
            }
            return radarData;
        }
    }
}