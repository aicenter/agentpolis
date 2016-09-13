package cz.agents.agentpolis.simmodel.environment.model.action.passenger;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.agents.agentpolis.simmodel.environment.model.VehiclePlanNotificationModel;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;

/**
 * Action makes un/registration of callbacks notified passenger by vehicle before next vehicle step   
 * 
 * @author Zbynek Moler
 *
 */
@Singleton
public class PassengerVehiclePlanAction{

	private final VehiclePlanNotificationModel vehiclePlanNotificationModel;	
	
	@Inject
	public PassengerVehiclePlanAction(
            VehiclePlanNotificationModel vehiclePlanNotificationModel) {
        super();
        this.vehiclePlanNotificationModel = vehiclePlanNotificationModel;
    }

    /**
	 * Passenger gets in vehicle -> passenger is linked with specific vehicle.
	 * @param passengerVehiclePlanCallback
	 */
	public void getInVehicle(String passengerId,VehicleArrivedCallback passengerVehiclePlanCallback){
		vehiclePlanNotificationModel.addPassengerVehiclePlanAndSensorCallbacksForNotify(passengerId, passengerVehiclePlanCallback);
	}
	
	/**
	 * Passenger gets off vehicle -> passenger is unlinked from vehicle, which using now.
	 */
	public void getOffVehicle(String passengerId){
		vehiclePlanNotificationModel.removePassengerVehiclePlanAndSensorCallback(passengerId);
	}
	

}
