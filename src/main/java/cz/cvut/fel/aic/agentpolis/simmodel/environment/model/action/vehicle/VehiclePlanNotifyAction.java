package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.vehicle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePlanNotificationModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;

/**
 * Action - un/registration of vehicle plan sensor
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehiclePlanNotifyAction {

    private final VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage;
    private final LinkedEntityModel linkedEntityStorage;

    @Inject
    public VehiclePlanNotifyAction(
            VehiclePlanNotificationModel passengerNotifyAboutVehiclePlanStorage,
            LinkedEntityModel linkedEntityStorage) {
        super();
        this.passengerNotifyAboutVehiclePlanStorage = passengerNotifyAboutVehiclePlanStorage;
        this.linkedEntityStorage = linkedEntityStorage;
    }

    /**
     * Informs waiting passenger and passenger in vehicle about next vehicle
     * plan.
     */

    public void notifyPassengerAndWatingPassenger(final String vehicleId, int fromNodeId,
                                                  int toNodeId, MovingActionCallback movingActionCallback) {
        for (String passengerId : linkedEntityStorage.getLinkedEntites(vehicleId)) {
            passengerNotifyAboutVehiclePlanStorage.notifyPassengerAboutVehiclePlan(passengerId,
                    fromNodeId, toNodeId, vehicleId, movingActionCallback);
        }

        passengerNotifyAboutVehiclePlanStorage.notifyWaitingPassengerAboutVehiclePlan(fromNodeId,
                toNodeId, vehicleId, movingActionCallback);

    }

}
