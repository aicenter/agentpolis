package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.configreader.ConfigReader;
import cz.agents.alite.configurator.Configurator;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.LocalSimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.ModuleSimulatorHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers.PlanCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.RoadNetworkRouter;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.XMLReader;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.HighwayStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.PlansOut;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandler;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.geographtools.Graph;
import org.apache.log4j.PropertyConfigurator;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.security.ProviderException;
import java.util.*;

@Singleton
public class AgentDriveModel {

    final Graph<SimulationNode, SimulationEdge> graph;
    private final AgentpolisConfig config;

    private final SimulationProvider simulationProvider;

    final TimeProvider timeProvider;

    private PlansOut plansOut = new PlansOut();
    private HighwayEnvironment highwayEnvironment;
    private RoadNetwork roadNetwork;
    private boolean initialPlansReceived = false;
    private PlanCallback planCallback;


    public static String CONFIG_FILE = "settings/groovy/highway.groovy";


    @Inject
    public AgentDriveModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                           SimulationProvider simulationProvider, TimeProvider timeProvider/*, ShapeUtils shapeUtils*/)
            throws ModelConstructionFailedException, ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        init();
        planCallback = new PlanCallbackImp();

        initRoadNetwork();
        highwayEnvironment = new HighwayEnvironment(simulationProvider.getSimulation(), roadNetwork);
        initTraffic();

        System.out.println(simulationProvider.getSimulation().toString());
        simulationProvider.getSimulation().addEventHandler(new EventHandler() {
            @Override
            public EventProcessor getEventProcessor() {
                return simulationProvider.getSimulation();
            }

            @Override
            public void handleEvent(Event event) {
                if (event.isType(AgentdriveEventType.INITIALIZE)) {
                    initialPlansReceived = true;
                    highwayEnvironment.getStorage().setSTARTTIME(getEventProcessor().getCurrentTime());
                    ADMessage adMessage = (ADMessage) event.getContent();
                    highwayEnvironment.updateCars(ADMessageToRadarData(adMessage));
                    getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData());
                } else if (event.isType(AgentdriveEventType.UPDATE_PLAN)) {
                    ADMessage adMessage = (ADMessage) event.getContent();
                    //TODO: change plans, decide until when is agent able to modify its route
                } else if (event.isType(AgentdriveEventType.DATA)) {
                    if (HighwayStorage.isFinished) {
                        getEventProcessor().addEvent(AgentdriveEventType.FINISH, null, null, null);
                    }else {
                        if (highwayEnvironment.getStorage().getCounter() > 0){highwayEnvironment.updateCars(highwayEnvironment.getStorage().getCurrentRadarData());

                        getEventProcessor().addEvent(AgentdriveEventType.DATA, null, null, highwayEnvironment.getStorage().getCurrentRadarData(), 10);
                    }}
                }
             }
        });
    }


    private void initRoadNetwork() {
        XMLReader xmlReader = new XMLReader();
        roadNetwork = xmlReader.parseNetwork(Configurator.getParamString("simulator.net.folder", "nets/junction-big/"));
        RoadNetworkRouter.setRoadNet(roadNetwork);
    }

    private void init() {

        // Configuration loading using alite's Configurator and ConfigReader
        ConfigReader configReader = new ConfigReader();

        configReader.loadAndMerge(CONFIG_FILE);
        Configurator.init(configReader);

        String logfile = Configurator.getParamString("cz.highway.configurationFile", "settings/log4j/log4j.properties");
        PropertyConfigurator.configure(logfile);
    }

    private void initTraffic() {
        final XMLReader reader = new XMLReader(Configurator.getParamString("simulator.net.folder", "notDefined"));
        // All vehicle id's
        final Collection<Integer> vehicles = reader.getRoutes().keySet();
        final Map<Integer, Float> departures = reader.getDepartures();
        final int size;
        if (!Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
            size = Configurator.getParamInt("highway.dashboard.numberOfCarsInSimulation", vehicles.size());
        } else {
            size = vehicles.size();
        }
        final HighwayStorage storage = highwayEnvironment.getStorage();

        Iterator<Integer> vehicleIt = vehicles.iterator();
        Set<Integer> plannedVehiclesLocal = new HashSet<Integer>();
        int sizeL = size;
        if (size > vehicles.size()) sizeL = vehicles.size();

        for (int i = 0; i < sizeL; i++) {
            int vehicleID = vehicleIt.next();
            if (Configurator.getParamBool("highway.dashboard.sumoSimulation", true)) {
                storage.addForInsert(vehicleID, departures.get(vehicleID));
            } else {
                storage.addForInsert(vehicleID);
            }
            plannedVehiclesLocal.add(vehicleID);
        }
        final Set<Integer> plannedVehicles = plannedVehiclesLocal;
        highwayEnvironment.addSimulatorHandler(new ModuleSimulatorHandler(highwayEnvironment, new HashSet<Integer>(plannedVehicles), planCallback));
    }

    public HighwayEnvironment getHighwayEnvironment() {
        return highwayEnvironment;
    }

    private RadarData ADMessageToRadarData(ADMessage adMessage) {
        //TODO
        return new RadarData();
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
                    int lane = highwayEnvironment.getRoadNetwork().getClosestLane(myPosition).getIndex();
                    state = new RoadObject(carID, highwayEnvironment.getCurrentTime(), lane, myPosition, vel);
                    radarData.add(state);
                    duration = 0;
                }
            }
            return radarData;
        }
    }
}
