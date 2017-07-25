package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.passenger.PassengerVehiclePlanAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;

@Singleton
public class GetInVehicleAction {

    private final VehicleStorage vehicleStorage;
    private final LinkedEntityModel linkedEntityStorage;
    private final PassengerVehiclePlanAction passengerVehiclePlanAction;

    @Inject
    public GetInVehicleAction(VehicleStorage vehicleStorage, LinkedEntityModel linkedEntityStorage,
            PassengerVehiclePlanAction passengerVehiclePlanAction) {
        super();
        this.vehicleStorage = vehicleStorage;
        this.linkedEntityStorage = linkedEntityStorage;
        this.passengerVehiclePlanAction = passengerVehiclePlanAction;
    }

    /**
     * Through this method - get in vehicle
     * 
     * 
     */
    public boolean getInVehicle(final String agentId, String vehicleId,
            VehicleArrivedCallback vehiclePlanCallback,
            LinkedEntitySensor linkedEntityCallback) {

        if (hasNotVehicleCapacityFree(vehicleId)) {
            return false;
        }

        passengerVehiclePlanAction.getInVehicle(agentId, vehiclePlanCallback);
        linkedEntityStorage.linkEnities(vehicleId, agentId, linkedEntityCallback);

        return true;

    }

    /**
     * 
     * Verifies possibility to get in vehicle, if vehicle capacity is full
     * return false.
     * 
     * @param vehicleId
     * @return
     */
    private boolean hasNotVehicleCapacityFree(String vehicleId) {
        PhysicalVehicle vehicle = vehicleStorage.getEntityById(vehicleId);
        int currentCapacityOfVehicle = linkedEntityStorage.numOfLinkedEntites(vehicleId);
        if ((currentCapacityOfVehicle + 1) > vehicle.getCapacity()) {
            return true;
        }
        return false;
    }

}
