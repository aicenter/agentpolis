package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.vehicle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehicleTimeModel;

/**
 * Action for set and remove day flag - which is using vehicle to know, if
 * moving over midnight
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleTimeAction {

    private final VehicleTimeModel vehicleTimeStorage;

    @Inject
    public VehicleTimeAction(VehicleTimeModel vehicleTimeStorage) {
        super();
        this.vehicleTimeStorage = vehicleTimeStorage;
    }

    /**
     * Sets flag, which represents day in simulation
     * 
     * @param dayFlag
     */
    public void setVehicleStartDayFlag(String vehicleId, long dayFlag) {
        vehicleTimeStorage.addVehicleDepartureDayFlag(vehicleId, dayFlag);
    }

    /**
     * Removes current set flag for vehicle
     */
    public void removeCurrentStoreVehicleStartDayFlag(String vehicleId) {
        vehicleTimeStorage.removeVehicleDepartureDayFlag(vehicleId);
    }

}
