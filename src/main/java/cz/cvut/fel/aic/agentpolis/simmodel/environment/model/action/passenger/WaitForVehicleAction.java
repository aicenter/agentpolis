package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.passenger;

import java.util.Set;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehicleGroupModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePlanNotificationModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;

/**
 * When passenger want to wait to vehicle.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class WaitForVehicleAction {

    private final VehicleGroupModel vehicleGroupStorage;
    private final VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage;

    @Inject
    public WaitForVehicleAction(VehicleGroupModel vehicleGroupStorage,
            VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage) {
        super();
        this.vehicleGroupStorage = vehicleGroupStorage;
        this.passengerNotifyAboutVehiclePlanStorage = passengerNotifyAboutVehiclePlanStorage;
    }

    /**
     * 
     * Waiting for specific vehicle, by vehicle id
     * 
     * @param vehicleId
     * @param waitingOnNodeById
     * @param vehiclePlanCallback
     */
    public void waitToVehicle(String passengerId, String vehicleId, long waitingOnNodeById,
            VehicleArrivedCallback vehiclePlanCallback) {

        passengerNotifyAboutVehiclePlanStorage.addWaitingForSpecificVehicle(passengerId, vehicleId,
                waitingOnNodeById, vehiclePlanCallback);
    }

    /**
     * Waiting for first arrived vehicle from group of vehicle
     * 
     * @param groupId
     * @param waitingOnNodeById
     * @param vehiclePlanCallback
     */
    public void waitToVehicleFromGroup(String passengerId, String groupId, long waitingOnNodeById,
            VehicleArrivedCallback vehiclePlanCallback) {

        Set<String> vehicleIdsWithTheSameGroupId = vehicleGroupStorage
                .getVehiclesFromGroup(groupId);

        passengerNotifyAboutVehiclePlanStorage.addWaitingForVehicleFromGroup(passengerId,
                vehicleIdsWithTheSameGroupId, waitingOnNodeById, vehiclePlanCallback);

    }

}
