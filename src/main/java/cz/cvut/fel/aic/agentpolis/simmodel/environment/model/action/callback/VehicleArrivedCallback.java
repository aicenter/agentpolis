package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback;

/**
 * 
 * New terminology: It is both VehicleArrived and NexVehicleLog
 * 
 * Callback method inform passenger about vehicle, on which waiting or using
 * 
 * @author Zbynek Moler
 * 
 */
public interface VehicleArrivedCallback {

    /**
     * It is invoked when vehicle arrived on place, where passenger waiting for
     * vehicle
     * 
     * @param fromNodeId
     * @param toNodeId
     * @param vehicleId
     */
    public void notifyWaitingPassengerAboutVehiclePlan(int fromNodeId, int toNodeId,
                                                       String vehicleId);

    /**
     * 
     * It is invoked before vehicle next move
     * 
     * @param fromNodeId
     * @param toNodeId
     * @param vehicleId
     */
    public void notifyPassengerAboutVehiclePlan(int fromNodeId, int toNodeId, String vehicleId);

}
