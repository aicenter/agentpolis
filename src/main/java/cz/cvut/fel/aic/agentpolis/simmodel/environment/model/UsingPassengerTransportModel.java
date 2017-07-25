package cz.cvut.fel.aic.agentpolis.simmodel.environment.model;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.sensor.UsingPublicTransportActivityCallback;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The model information passenger about change during using public trnasport
 * 
 * (The models are not in new terminology, the environment objects are instead
 * of the models)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class UsingPassengerTransportModel {

    private final Map<String, PassengerActivityCallback<?>> passengerTripCallbacks;
    private final Map<String, UsingPublicTransportActivityCallback> usingPublicTransportActivityCallbacks;
    private final EventProcessor eventProcessor;

    @Inject
    public UsingPassengerTransportModel(
            Map<String, PassengerActivityCallback<?>> passengerTripCallbacks,
            Map<String, UsingPublicTransportActivityCallback> usingPublicTransportActivityCallbacks,
            EventProcessor eventProcessor) {
        super();
        this.passengerTripCallbacks = passengerTripCallbacks;
        this.usingPublicTransportActivityCallbacks = usingPublicTransportActivityCallbacks;
        this.eventProcessor = eventProcessor;
    }

    public void addUsingPublicTransportCallBack(String passengerId,
            UsingPublicTransportActivityCallback usingPublicTransportActivityCallback) {
        usingPublicTransportActivityCallbacks
                .put(passengerId, usingPublicTransportActivityCallback);
    }

    public void addPassengerTripCallback(String passengerId,
            PassengerActivityCallback<?> passengerActivityCallback) {
        passengerTripCallbacks.put(passengerId, passengerActivityCallback);
    }

    public void finishedUsingPublicTransport(String passengerId) {
        UsingPublicTransportActivityCallback publicTransportActivityCallback = usingPublicTransportActivityCallbacks
                .get(passengerId);
        finishedUsingPublicTransport(publicTransportActivityCallback);
    }

    public void doneFullTrip(String passengerId) {
        PassengerActivityCallback<?> passengerActivityCallback = passengerTripCallbacks
                .get(passengerId);
        doneFullTrip(passengerActivityCallback);

    }

    public <TTrip extends GraphTrip<?>> void donePartTrip(String passengerId, final TTrip partNotDoneTrip) {
        @SuppressWarnings("unchecked")
        PassengerActivityCallback<TTrip> passengerActivityCallback = (PassengerActivityCallback<TTrip>) passengerTripCallbacks
                .get(passengerId);
        donePartTrip(passengerActivityCallback, partNotDoneTrip);

    }

    public <TTrip extends GraphTrip<?>> void tripFail(String passengerId, final TTrip failedTrip) {
        @SuppressWarnings("unchecked")
        PassengerActivityCallback<TTrip> passengerActivityCallback = (PassengerActivityCallback<TTrip>) passengerTripCallbacks
                .get(passengerId);
        tripFail(passengerActivityCallback, failedTrip);

    }

    // --------- callback callers

    private <TTrip extends GraphTrip<?>> void donePartTrip(
            final PassengerActivityCallback<TTrip> passengerActivityCallback,
            final TTrip partNotDoneTrip) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerActivityCallback.donePartTrip(partNotDoneTrip);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });
    }

    private <TTrip extends GraphTrip<?>> void tripFail(
            final PassengerActivityCallback<TTrip> passengerActivityCallback, final TTrip failedTrip) {
        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerActivityCallback.tripFail(failedTrip);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });

    }

    private void doneFullTrip(final PassengerActivityCallback<?> passengerActivityCallback) {
        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerActivityCallback.doneFullTrip();
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });
    }

    private void finishedUsingPublicTransport(
            final UsingPublicTransportActivityCallback usingPublicTransportActivityCallback) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                usingPublicTransportActivityCallback.finishedUsingPublicTransport();
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });
    }

}
