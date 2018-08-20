package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.SimulatorHandlers;


import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RadarData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.PlansOut;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * Created by david on 7/13/16.
 */
public abstract class PlanCallback extends Observable {
    private Set<Integer> plannedVehiclesToRemove = new HashSet<>();
    public abstract RadarData execute(PlansOut plans);
    public void addToPlannedVehiclesToRemove(Integer id){
        plannedVehiclesToRemove.add(id);
    }
    public void clearPlannedVehicles(){
        plannedVehiclesToRemove.clear();
    }
    public Set<Integer> getPlannedVehiclesToRemove(){
        return plannedVehiclesToRemove;
    }
}
