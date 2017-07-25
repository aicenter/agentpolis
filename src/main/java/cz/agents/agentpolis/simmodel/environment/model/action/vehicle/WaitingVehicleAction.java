package cz.agents.agentpolis.simmodel.environment.model.action.vehicle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.agents.agentpolis.simmodel.environment.model.VehicleTimeModel;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.publictransport.TimetableItem;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/***
 * 
 * Provides methods for waiting to departure time.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class WaitingVehicleAction {

    private final EventProcessor eventProcessor;
    private final VehicleTimeModel vehicleTimeStorage;
    private final StandardTimeProvider timeEnvUtil;

    @Inject
    public WaitingVehicleAction(EventProcessor eventProcessor, VehicleTimeModel vehicleTimeStorage,
            StandardTimeProvider timeEnvUtil) {
        super();
        this.eventProcessor = eventProcessor;
        this.vehicleTimeStorage = vehicleTimeStorage;
        this.timeEnvUtil = timeEnvUtil;
    }

    /**
     * Waiting to departure time
     * 
     * @param fromNode
     * @param toNode
     * @param departureTime
     * @param movingActionCallback
     */
    public void waitToDepartureTime(String vehicleId, final int fromNode, final int toNode,
                                    TimetableItem departureTime, final MovingActionCallback movingActionCallback) {
        waitToDepartureTime(vehicleId, departureTime, new WaitToDepartureTimeEventHandler(fromNode,
                toNode, movingActionCallback));

    }

    private void waitToDepartureTime(String vehicleId, TimetableItem departureTime,
            WaitingToDepartureTime waitingToDepartureTime) {
        long waitTime = computeWaitTime(vehicleId, departureTime);
        if (waitTime > 1) {
            eventProcessor.addEvent(waitingToDepartureTime, waitTime);
        } else {
            waitingToDepartureTime.doNodeWaitingToDepartureTime();
        }
    }

    private long computeWaitTime(String vehicleId, TimetableItem departureTime) {
        long previousDayFlag = vehicleTimeStorage.getVehicleDepartureDayFlag(vehicleId);

        long waitTime = 0;
        if (departureTime.overMidnight) {
            waitTime = timeEnvUtil.computeDepartureTimeOverMidnight(previousDayFlag,
                    departureTime.time);
        } else {
            waitTime = timeEnvUtil.computeDepartureTime(previousDayFlag, departureTime.time);
        }

        if (waitTime < 0) {
            return 0;
        }

        return waitTime;
    }

    private interface WaitingToDepartureTime extends EventHandler {

        public void doNodeWaitingToDepartureTime();

    }

    private class WaitToDepartureTimeEventHandler implements WaitingToDepartureTime {

        private final int fromNode;
        private final int toNode;
        private final MovingActionCallback movingActionCallback;

        public WaitToDepartureTimeEventHandler(int fromNode, int toNode,
                                               MovingActionCallback movingActionCallback) {
            super();
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.movingActionCallback = movingActionCallback;
        }

        @Override
        public EventProcessor getEventProcessor() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void handleEvent(Event event) {
            endWaitingToDepartureTime();
        }

        @Override
        public void doNodeWaitingToDepartureTime() {
            endWaitingToDepartureTime();

        }

        private void endWaitingToDepartureTime() {
            movingActionCallback.endWaitingToDepartureTime(fromNode, toNode);
        }

    }

}
