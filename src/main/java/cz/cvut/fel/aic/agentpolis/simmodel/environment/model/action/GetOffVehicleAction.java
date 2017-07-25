package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.passenger.PassengerVehiclePlanAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;

@Singleton
public class GetOffVehicleAction {

    private final LinkedEntityModel linkedEntityStorage;
    private final PassengerVehiclePlanAction passengerVehiclePlanAction;

    @Inject
    public GetOffVehicleAction(LinkedEntityModel linkedEntityStorage,
            PassengerVehiclePlanAction passengerVehiclePlanAction) {
        super();
        this.linkedEntityStorage = linkedEntityStorage;
        this.passengerVehiclePlanAction = passengerVehiclePlanAction;
    }

    /**
     * Passenger can get off vehicle
     * 
     * @param vehicleId
     */
    public void getOffVehicleAndUnLink(final String agentId, final String vehicleId) {
        passengerVehiclePlanAction.getOffVehicle(agentId);
        linkedEntityStorage.unLinkEnitiesWithoutCalling(agentId);
    }

    /**
     * Through this method agent can get off a vehicle.
     */
    public void getOffVehicle(final String agentId) {
        passengerVehiclePlanAction.getOffVehicle(agentId);
    }
}
