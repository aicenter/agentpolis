package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.HighwayEnvironment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by david on 7/13/16.
 */
public class ModuleSimulatorHandler extends SimulatorHandler {
    protected PlanCallback planCallback;
    public ModuleSimulatorHandler(HighwayEnvironment highwayEnvironment, Set<Integer> plannedVehicles, PlanCallback planCallback) {
        super(highwayEnvironment,plannedVehicles);
        this.planCallback = planCallback;

    }
    @Override
    public void sendPlans(Map<Integer, RoadObject> vehicleStates) {
        RadarData radarData = new RadarData();

        Set<Integer> notPlanned = new HashSet<Integer>(vehicleStates.keySet());
        plannedVehicles.removeAll(planCallback.getPlannedVehiclesToRemove());

        planCallback.getPlannedVehiclesToRemove().clear();
        notPlanned.removeAll(plannedVehicles);
        planCallback.clearPlannedVehicles();
        for (int id : notPlanned) {
            radarData.add(vehicleStates.get(id));
        }
        plans.setCurrStates(vehicleStates);
        // Finally send plans and updates
        newRadarData = planCallback.execute(plans);
        plans.initNewPlans();

    }
    public boolean hasVehicle(int id)
    {
        return true;
    }

}
