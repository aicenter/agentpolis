package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.PlansOut;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by david on 9/11/15.
 */
public class LocalSimulatorHandler extends SimulatorHandler {

    public LocalSimulatorHandler(HighwayEnvironment highwayEnvironment, Set<Integer> plannedVehicles) {
        super(highwayEnvironment, plannedVehicles);
    }

    @Override
    public void sendPlans(Map<Integer, RoadObject> vehicleStates) {
        RadarData radarData = new RadarData();

        Set<Integer> notPlanned = new HashSet<Integer>(vehicleStates.keySet());
        notPlanned.removeAll(plannedVehicles);

        for (int id : notPlanned) {
            radarData.add(vehicleStates.get(id));
        }

        // Finally send plans and updates
        newRadarData = executePlans(plans);
        plans.clear();
    }

    private RadarData executePlans(PlansOut plans) {
        Map<Integer, RoadObject> currStates = highwayEnvironment.getStorage().getPosCurr();
        RadarData radarData = new RadarData();
        float duration = 0;
        float lastDuration = 0;
        double timest = AgentDriveModel.adConfig.timestep;
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
                plannedVehicles.remove(carID);
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
        //send radar-data to storage with duration delay
//        highwayEnvironment.getEventProcessor().addEvent(HighwayEventType.RADAR_DATA, highwayEnvironment.getStorage(), null, radarData, Math.max(1, (long) (timestep * 1000)));
    }
}
