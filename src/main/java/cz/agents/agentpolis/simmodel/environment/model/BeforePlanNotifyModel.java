package cz.agents.agentpolis.simmodel.environment.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PassengerBeforePlanNotifySensor;
import cz.agents.agentpolis.utils.InitAndGetterUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The passenger notification model which informs passenger before vehicle next
 * plan notification
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class BeforePlanNotifyModel {

    // Key = String = passenger id
    private final Map<String, PassengerBeforePlanNotifySensor> passengersForInformBeforePlanNotify;
    // Key = String = vehicle id , Value = Set of strings = passenger ids
    private final Map<String, Set<String>> currentlyInformPassengersBeforePlanNotify;
    // Key = String = passenger id , Value = String = vehicle id
    private final Map<String, String> passengerIdLinkedWithVehicleId;
    // Key = String = vehicle id
    private final Map<String, MovingActionCallback> vehicleIdAndItsMovementCallback;

    private final EventProcessor eventProcessor;

    @Inject
    public BeforePlanNotifyModel(
            Map<String, PassengerBeforePlanNotifySensor> passengersForInformBeforePlanNotify,
            Map<String, Set<String>> currentlyInformPassengersBeforePlanNotify,
            Map<String, String> passengerIdLinkedWithVehicleId,
            Map<String, MovingActionCallback> vehicleIdAndItsMovementCallback,
            EventProcessor eventProcessor) {

        this.passengersForInformBeforePlanNotify = passengersForInformBeforePlanNotify;
        this.currentlyInformPassengersBeforePlanNotify = currentlyInformPassengersBeforePlanNotify;
        this.passengerIdLinkedWithVehicleId = passengerIdLinkedWithVehicleId;
        this.vehicleIdAndItsMovementCallback = vehicleIdAndItsMovementCallback;
        this.eventProcessor = eventProcessor;
    }

    public void addPassengerForInform(String passengerId,
            PassengerBeforePlanNotifySensor beforePlanNotifySensorCallback) {
        passengersForInformBeforePlanNotify.put(passengerId, beforePlanNotifySensorCallback);
    }

    public void removePassengerForInform(String passengerId) {
        passengersForInformBeforePlanNotify.remove(passengerId);
    }

    public void callBeforePlanNotify(String vehicleId, long fromPositionByNodeId,
            long toPositionByNodeId, Set<String> passengersIncludedDriverInVehilce,
            MovingActionCallback movingActionCallback) {

        Set<String> passengers = InitAndGetterUtil.getDataOrInitFromMap(
                currentlyInformPassengersBeforePlanNotify, vehicleId, new HashSet<String>());

        assert passengers.isEmpty() : "set of passenger has to be empty";

        for (String passengerId : passengersIncludedDriverInVehilce) {
            if (passengersForInformBeforePlanNotify.keySet().contains(passengerId)) {
                passengers.add(passengerId);
                passengerIdLinkedWithVehicleId.put(passengerId, vehicleId);
                passengersForInformBeforePlanNotify.get(passengerId).beforePlanNotify(passengerId,
                        vehicleId, fromPositionByNodeId, toPositionByNodeId);
            }
        }

        currentlyInformPassengersBeforePlanNotify.put(vehicleId, passengers);

        if (passengers.isEmpty()) {
            endBeforePlanNotify(movingActionCallback);
        } else {
            vehicleIdAndItsMovementCallback.put(vehicleId, movingActionCallback);
        }

    }

    public void afterLastPassengerForInformInvokeCallbackForVehicle(String passengerId) {

        String vehicleId = passengerIdLinkedWithVehicleId.remove(passengerId);

        Set<String> passengers = currentlyInformPassengersBeforePlanNotify.get(vehicleId);
        passengers.remove(passengerId);

        if (passengers.isEmpty()) {
            MovingActionCallback movementActionCallback = vehicleIdAndItsMovementCallback
                    .remove(vehicleId);
            endBeforePlanNotify(movementActionCallback);
        }

        currentlyInformPassengersBeforePlanNotify.put(vehicleId, passengers);

    }

    // ----------------------------------------------------------------------

    private void endBeforePlanNotify(final MovingActionCallback movingActionCallback) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                movingActionCallback.endBeforeNotifyingAboutPlan();

            }

            @Override
            public EventProcessor getEventProcessor() {
                // TODO Auto-generated method stub
                return null;
            }
        });

    }

}
