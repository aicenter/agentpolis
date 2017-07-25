package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.UsingPassengerTransportModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.sensor.UsingPublicTransportActivityCallback;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * Action provides methods, which associate with using public transport and
 * informs about executed trip.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class PassengerTripAction {

    private final EventProcessor eventProcessor;
    private final UsingPassengerTransportModel passengerTripStorage;

    @Inject
    public PassengerTripAction(EventProcessor eventProcessor,
            UsingPassengerTransportModel passengerTripStorage) {
        super();
        this.eventProcessor = eventProcessor;
        this.passengerTripStorage = passengerTripStorage;
    }

    /**
     * 
     * Adds to passenger storage callback - for using public transport
     * 
     * @param usingPublicTransportActivityCallback
     */
    public void addUsingPublicTransportCallBack(final String passengerId,
            UsingPublicTransportActivityCallback usingPublicTransportActivityCallback) {
        passengerTripStorage.addUsingPublicTransportCallBack(passengerId,
                usingPublicTransportActivityCallback);
    }

    /**
     * Adds to passenger storage callback - for passenger activity
     * 
     * @param passengerActivityCallback
     */
    public void addPassengerTripCallback(final String passengerId,
            PassengerActivityCallback<?> passengerActivityCallback) {
        passengerTripStorage.addPassengerTripCallback(passengerId, passengerActivityCallback);
    }

    /**
     * Through this method is passenger informs about finish using public
     * transport.
     */
    public void finishedUsingPublicTransport(final String passengerId) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerTripStorage.finishedUsingPublicTransport(passengerId);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });

    }

    /**
     * Passenger is informed about executed his full trip
     */
    public void doneFullTrip(final String passengerId) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerTripStorage.doneFullTrip(passengerId);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });

    }

    /**
     * Passenger is informed, that was not possible to execute his trip
     */
    public <TTrip extends GraphTrip<?>> void tripFail(final String passengerId, final TTrip failedTrip) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerTripStorage.tripFail(passengerId, failedTrip);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });

    }

    /**
     * Passenger is informed, that was executed just part of trip
     */
    public <TTrip extends GraphTrip<?>> void donePartTrip(final String passengerId,
            final TTrip partNotDoneTrip) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                passengerTripStorage.donePartTrip(passengerId, partNotDoneTrip);
            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        });

    }

}
