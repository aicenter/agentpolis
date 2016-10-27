package cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayActor;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayModel;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayingSegment;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.JunctionHandler;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.dto.JunctionHandlingRusult;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.dto.ResultOfMovingFromDelayToWaitingQueue;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.FromToPositionKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.JunctionPositionKey;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The implementation of {@code DelayModel} handles the incoming
 * {@code DelayActor}s and executes {@code DelayingSegment} which contains
 * {@code DelayActor}s
 * 
 * @author Zbynek Moler
 * 
 */
public class DelayModelImpl implements DelayModel {

    private final JunctionHandler junctionHandler;
    private final DelayModelStorage queueStorage;
    private final EventProcessor eventProcessor;

    private final Set<FromToPositionKey> calledQueueGetterForFRomToNodeAndTime = new HashSet<FromToPositionKey>();
    private final Set<JunctionPositionKey> calledQueueGetterForSpecificNodeAndTime = new HashSet<JunctionPositionKey>();

    private final Set<FromToPositionKey> waitingForReleasingDirection = new HashSet<FromToPositionKey>();

    private static final long CALL_QUEUE_GETTER_IN_NEXT_STEP = 1;

    public DelayModelImpl(
            Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> junctions,
            EventProcessor eventProcessor, JunctionHandler junctionHandler) {
        super();
        this.junctionHandler = junctionHandler;
        this.queueStorage = new DelayModelStorage(junctions);
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void handleDelayActor(long fromByNodeId, long toByNodeId, GraphType graphType,
            DelayActor delayActor) {

        queueStorage.addQueueItems(fromByNodeId, toByNodeId, graphType, delayActor);

        callQueueGetter(fromByNodeId, toByNodeId, graphType);
    }

    private void callQueueGetter(long fromByNodeId, long toByNodeId, GraphType graphType) {
        callQueueGetter(fromByNodeId, toByNodeId, CALL_QUEUE_GETTER_IN_NEXT_STEP, graphType);

    }

    private void callCroassroadQueueGetter(long crossroadsByNodeId, GraphType graphType) {
        JunctionPositionKey crossroadsPositionKey = new JunctionPositionKey(crossroadsByNodeId);

        if (calledQueueGetterForSpecificNodeAndTime.contains(crossroadsPositionKey) == false) {
            calledQueueGetterForSpecificNodeAndTime.add(crossroadsPositionKey);
            eventProcessor.addEvent(new JunctionGetter(crossroadsByNodeId, graphType));
        }

    }

    /**
     * If direction from-to is not to set for handling in next step, then this
     * method sets it to event processor.
     * 
     * @param fromNodeById
     * @param toByNodeId
     * @param timeCallingQueueGetter
     * @param graphType
     */
    private void callQueueGetter(long fromNodeById, long toByNodeId, long timeCallingQueueGetter,
            GraphType graphType) {

        FromToPositionKey fromToPositionKey = new FromToPositionKey(fromNodeById, toByNodeId);

        if (calledQueueGetterForFRomToNodeAndTime.contains(fromToPositionKey) == false) {
            calledQueueGetterForFRomToNodeAndTime.add(fromToPositionKey);
            eventProcessor.addEvent(new QueueFromToGetter(fromNodeById, toByNodeId, graphType),
                    timeCallingQueueGetter);
        }

    }

    private class JunctionGetter implements EventHandler {

        private final long crossroadByNodeId;
        private final GraphType graphType;

        public JunctionGetter(long crossroadByNodeId, GraphType graphType) {
            super();
            this.crossroadByNodeId = crossroadByNodeId;
            this.graphType = graphType;
        }

        @Override
        public EventProcessor getEventProcessor() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void handleEvent(Event event) {
            calledQueueGetterForSpecificNodeAndTime.remove(new JunctionPositionKey(
                    crossroadByNodeId));

            JunctionHandlingRusult crossroadResolvingResult = junctionHandler.handleJunction(
                    crossroadByNodeId, graphType, queueStorage);

            waitingForReleasingDirection
                    .addAll(crossroadResolvingResult.waitingForReleasingDirection);

            for (FromToPositionKey key : crossroadResolvingResult.releasedDirectionToCrossroad) {

                if (waitingForReleasingDirection.contains(key)) {
                    waitingForReleasingDirection.remove(key);
                    callCroassroadQueueGetter(key.fromPositionByNodeId, graphType);
                }
            }

        }

    }

    private class QueueFromToGetter implements EventHandler {

        private final long fromByNodeId;
        private final long toByNodeId;
        private final GraphType graphType;

        public QueueFromToGetter(long fromByNodeId, long toByNodeId, GraphType graphType) {
            super();
            this.fromByNodeId = fromByNodeId;
            this.toByNodeId = toByNodeId;
            this.graphType = graphType;
        }

        @Override
        public EventProcessor getEventProcessor() {
            return eventProcessor;
        }

        @Override
        public void handleEvent(Event event) {

            calledQueueGetterForFRomToNodeAndTime.remove(new FromToPositionKey(fromByNodeId,
                    toByNodeId));

            ResultOfMovingFromDelayToWaitingQueue result = queueStorage
                    .moveFromInToOutQueueAndReturnQueueDelay(fromByNodeId, toByNodeId, graphType);

            if (result.wasMoved) {
                callQueueGetter(fromByNodeId, toByNodeId, result.queueDelay, graphType);
                callCroassroadQueueGetter(toByNodeId, graphType);

            }

        }

    }

}
