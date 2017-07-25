package cz.cvut.fel.aic.agentpolis.simmodel.environment.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.key.VehicleAndPositionKey;
import cz.agents.agentpolis.utils.InitAndGetterUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import cz.agents.alite.common.event.EventProcessor;

/**
 * The model notifies about a plan of a particular vehicle
 * <p>
 * (The models are not in new terminology, the environment objects are instead of the models)
 *
 * @author Zbynek Moler
 */
@Singleton
public class VehiclePlanNotificationModel {

	private final Logger log = Logger.getLogger(VehiclePlanNotificationModel.class);

	private final Map<VehicleAndPositionKey, Set<String>> waitingPassengersOnSpecificPosition;
	private final Map<String, VehicleArrivedCallback> passengerAndVehiclePlanCallback;
	private final Map<String, PassengerWaitingVehicle> passengerWaitingSensorAndVehiclePlanCallback = new HashMap<>();

	private final Map<String, Set<String>> vehicleByIdAndCurrentNotifyingEntities = new HashMap<>();
	private final Map<String, MovingActionCallback> vehicleAndMovingActionCallback = new HashMap<>();

	private final EventProcessor eventProcessor;

	private final PassengerWaitingVehicle passengerWaitingVehicle = this::notifyWaitingPassenger;
    
    private final HashMap<String,VehicleAndPositionKey> zillionthDammHelperMap = new HashMap<>();

	@Inject
	public VehiclePlanNotificationModel(Map<VehicleAndPositionKey, Set<String>> waitingPassengersOnSpecificPosition,
										Map<String, VehicleArrivedCallback> passengerAndVehiclePlanCallback,
										EventProcessor eventProcessor) {
		super();

		this.waitingPassengersOnSpecificPosition = waitingPassengersOnSpecificPosition;
		this.passengerAndVehiclePlanCallback = passengerAndVehiclePlanCallback;
		this.eventProcessor = eventProcessor;

	}

	public void stopWaitingPassenger(String passengerId, String vehicleId, long waitingPositionByNodeId) {
		VehicleAndPositionKey key = new VehicleAndPositionKey(waitingPositionByNodeId, vehicleId);

		Set<String> waitingPassengersById = waitingPassengersOnSpecificPosition.get(key);
		waitingPassengersById.remove(passengerId);
		waitingPassengersOnSpecificPosition.put(key, waitingPassengersById);
        
        for(String passangerId : waitingPassengersById){
            zillionthDammHelperMap.put(passangerId, key);
        }

		passengerWaitingSensorAndVehiclePlanCallback.remove(passengerId);
		passengerAndVehiclePlanCallback.remove(passengerId);
	}

	/**
	 * Waiting for first arrived vehicle from group of vehicle
	 */
	public void addWaitingForVehicleFromGroup(String passengerId, Set<String> vehicleIdsWithTheSameGroupId,
											  long waitingOnNodeById, VehicleArrivedCallback vehiclePlanCallback) {

		if (vehicleIdsWithTheSameGroupId == null) {
			log.error("Expected a list of vehicle IDs within the same group (passenger ID=" + passengerId + "), got " +
					"null; " +
					"skipping (agent will probably be unable to travel)");
			return;
		}

		PassengerWaitingVehicleGroupSensor waitingVehicleGroupSensor = new PassengerWaitingVehicleGroupSensor
				(vehicleIdsWithTheSameGroupId);

		for (String vehicleId : vehicleIdsWithTheSameGroupId) {
			addWaitingForSpecificVehicle(passengerId, vehicleId, waitingOnNodeById, vehiclePlanCallback,
					waitingVehicleGroupSensor);
		}

	}

	// ---------------------------

	public void addWaitingForSpecificVehicle(String passengerId, String vehicleId, long waitngPositionByNodeId,
											 VehicleArrivedCallback vehiclePlanCallback) {

		addWaitingForSpecificVehicle(passengerId, vehicleId, waitngPositionByNodeId, vehiclePlanCallback,
				passengerWaitingVehicle);
	}

	private void addWaitingForSpecificVehicle(String passengerId, String vehicleId, long waitngPositionByNodeId,
											  VehicleArrivedCallback vehiclePlanCallback,
											  PassengerWaitingVehicle passengerWaitingVehicleSensorCallback) {

		VehicleAndPositionKey vehicleAndPositionKey = new VehicleAndPositionKey(waitngPositionByNodeId, vehicleId);

		Set<String> waitingPassengersById = waitingPassengersOnSpecificPosition.get(vehicleAndPositionKey);
		if (waitingPassengersById == null) {
			waitingPassengersById = new HashSet<>();
		}

		waitingPassengersById.add(passengerId);
		waitingPassengersOnSpecificPosition.put(vehicleAndPositionKey, waitingPassengersById);
        for(String passangerId : waitingPassengersById){
            zillionthDammHelperMap.put(passangerId, vehicleAndPositionKey);
        }

		passengerAndVehiclePlanCallback.put(passengerId, vehiclePlanCallback);
		passengerWaitingSensorAndVehiclePlanCallback.put(passengerId, passengerWaitingVehicleSensorCallback);

	}

	public void notifyWaitingPassengerAboutVehiclePlan(final int fromNodeId, final int toNodeId, final String vehicleId,
													   MovingActionCallback movingActionCallback) {

		vehicleAndMovingActionCallback.put(vehicleId, movingActionCallback);

		Set<String> waitingPassengersById = InitAndGetterUtil.getDataOrInitFromMap
				(waitingPassengersOnSpecificPosition, new VehicleAndPositionKey(fromNodeId, vehicleId), new HashSet<>
						());

		for (String waitingPassengerId : new HashSet<>(waitingPassengersById)) {

			addNotifyEntity(vehicleId, waitingPassengerId);

			VehicleArrivedCallback passengerVehiclePlanCallback 
                    = passengerAndVehiclePlanCallback.get(waitingPassengerId);
            if(passengerVehiclePlanCallback == null){
                try {
                    throw new Exception("passengerVehiclePlanCallback cannot be null");
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(VehiclePlanNotificationModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
			PassengerWaitingVehicle passengerWaitingVehicle = passengerWaitingSensorAndVehiclePlanCallback.get
					(waitingPassengerId);

			passengerWaitingVehicle.notifyWaitingPassengerAboutVehiclePlan(waitingPassengerId, fromNodeId, toNodeId,
					vehicleId, passengerVehiclePlanCallback);
		}

	}

	public void notifyPassengerAboutVehiclePlan(String passengerId, final int fromNodeId, final int toNodeId,
												final String vehicleId, MovingActionCallback movingActionCallback) {

		vehicleAndMovingActionCallback.put(vehicleId, movingActionCallback);

		addNotifyEntity(vehicleId, passengerId);

		VehicleArrivedCallback passengerVehiclePlanCallback = passengerAndVehiclePlanCallback.get(passengerId);

		notifyPassengerAboutVehiclePlan(passengerId, fromNodeId, toNodeId, vehicleId, passengerVehiclePlanCallback);

	}

	private void addNotifyEntity(String vehicleId, String passengerId) {

		Set<String> currentNotifyingPassengers = InitAndGetterUtil.getDataOrInitFromMap
				(vehicleByIdAndCurrentNotifyingEntities, vehicleId, new HashSet<>());
		currentNotifyingPassengers.add(passengerId);
		vehicleByIdAndCurrentNotifyingEntities.put(vehicleId, currentNotifyingPassengers);

	}

	public void notifiedVehicleIfRemovingLast(String vehicleId, String passengerId, int fromByNodeId, int toByNodeId) {

		Set<String> currentNotifyingPassengers = InitAndGetterUtil.getDataOrInitFromMap
				(vehicleByIdAndCurrentNotifyingEntities, vehicleId, new HashSet<>());
		currentNotifyingPassengers.remove(passengerId);

		if (currentNotifyingPassengers.isEmpty()) {
			notifiedPassengerAndWaitingPassenger(fromByNodeId, toByNodeId, vehicleAndMovingActionCallback.get
					(vehicleId));
		}

		vehicleByIdAndCurrentNotifyingEntities.put(vehicleId, currentNotifyingPassengers);
	}

	public void addPassengerVehiclePlanAndSensorCallbacksForNotify(String passengerId,
																   VehicleArrivedCallback
																		   passengerVehiclePlanCallback) {
		passengerAndVehiclePlanCallback.put(passengerId, passengerVehiclePlanCallback);
	}

	public void removePassengerVehiclePlanAndSensorCallback(String passengerId) {
        waitingPassengersOnSpecificPosition.remove(zillionthDammHelperMap.get(passengerId));
		passengerAndVehiclePlanCallback.remove(passengerId);
	}

	// --------------------------------------

	private void notifiedPassengerAndWaitingPassenger(final int fromByNodeId, final int toByNodeId,
													  final MovingActionCallback movingActionCallback) {

		eventProcessor.addEvent(new EventHandlerAdapter() {

			@Override
			public void handleEvent(Event event) {
				movingActionCallback.dependentEntitiesWereNotifiedAboutMovingPlan(fromByNodeId, toByNodeId);

			}

		});

	}

	private void notifyPassengerAboutVehiclePlan(final String passengerId, final int fromByNodeId, final int
			toByNodeId,
												 final String vehicleId,
												 VehicleArrivedCallback passengerVehiclePlanCallback) {

		passengerVehiclePlanCallback.notifyPassengerAboutVehiclePlan(fromByNodeId, toByNodeId, vehicleId);

		eventProcessor.addEvent(new EventHandlerAdapter() {

			@Override
			public void handleEvent(Event event) {
				notifiedVehicleIfRemovingLast(vehicleId, passengerId, fromByNodeId, toByNodeId);

			}
		});

	}

	private interface PassengerWaitingVehicle {

		public void notifyWaitingPassengerAboutVehiclePlan(final String passengerId, int fromNodeId, int toNodeId,
														   String vehicleId,
														   VehicleArrivedCallback passengerVehiclePlanCallback);

	}

	public class PassengerWaitingVehicleGroupSensor implements PassengerWaitingVehicle {

		private Set<String> vehicleIdsWiththaSameGroupId;

		public PassengerWaitingVehicleGroupSensor(Set<String> vehicleIdsWiththaSameGroupId) {
			super();
			this.vehicleIdsWiththaSameGroupId = vehicleIdsWiththaSameGroupId;
		}

		@Override
		public void notifyWaitingPassengerAboutVehiclePlan(final String passengerId, final int fromByNodeId,
														   final int toByNodeId, final String vehicleId,
														   VehicleArrivedCallback passengerVehiclePlanCallback) {

			for (String boundedVehicleById : vehicleIdsWiththaSameGroupId) {
				stopWaitingPassenger(passengerId, boundedVehicleById, fromByNodeId);
			}

			notifyWaitingPassenger(passengerId, fromByNodeId, toByNodeId, vehicleId, passengerVehiclePlanCallback);

		}

	}

	private void notifyWaitingPassenger(final String passengerId, final int fromByNodeId, final int toByNodeId,
										final String vehicleId, VehicleArrivedCallback passengerVehiclePlanCallback) {

		passengerVehiclePlanCallback.notifyWaitingPassengerAboutVehiclePlan(fromByNodeId, toByNodeId, vehicleId);

		eventProcessor.addEvent(new EventHandlerAdapter() {

			@Override
			public void handleEvent(Event event) {
				notifiedVehicleIfRemovingLast(vehicleId, passengerId, fromByNodeId, toByNodeId);
			}

		});
	}

}
