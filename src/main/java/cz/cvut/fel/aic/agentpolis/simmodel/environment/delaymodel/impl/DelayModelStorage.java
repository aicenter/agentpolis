package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayActor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayingSegment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.dto.ResultOfMovingFromDelayToWaitingQueue;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.FromToPositionKey;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.GraphTypeAndToNodeKey;

/**
 * Storage takes queues for each edge of all graphs.
 *
 * @author Zbynek Moler
 */
public class DelayModelStorage {

    private static final Logger LOGGER = Logger.getLogger(DelayModelStorage.class);

    private final Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> queues;

    public DelayModelStorage(
            Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> queues) {
        super();
        this.queues = queues;
    }

    /**
     * Adds {@code DelayActor} to specific queue.
     *
     * @param fromNodeById
     * @param toNodeById
     * @param graphType
     * @param queueItem
     * @return - true - if adding to empty queue
     */
    public boolean addQueueItems(long fromNodeById, long toNodeById, GraphType graphType,
                                 DelayActor queueItem) {
        GraphTypeFromToNodeKey key = createGraphTypeFromToNodeKey(graphType, fromNodeById,
                toNodeById);

        InnerQueueAndItems innerQueueAndItems = createInnerQueueAndItems(key);
        DelayingSegment queueItems = innerQueueAndItems.queueItems;

        if (queueItems == null) {
            LOGGER.debug("Problem at: " + fromNodeById + " " + toNodeById);
        }

        boolean firstAddingOnToNode = queueItems.isDelayQueueEmpty();
        queueItems.addDelayActor(queueItem);

        setDatatToInnerQueueToQueue(key, queueItems, innerQueueAndItems.innerQueues);

        return firstAddingOnToNode;
    }

    /**
     * Get queues for specific node (crossroad)
     *
     * @param graphType
     * @param toNodeById
     * @return
     */
    public Map<GraphTypeAndFromToNodeKey, DelayingSegment> getQueueItems(GraphType graphType,
                                                                         long toNodeById) {
        GraphTypeAndToNodeKey graphTypeAndToNodeKey = new GraphTypeAndToNodeKey(graphType,
                toNodeById);
        return queues.get(graphTypeAndToNodeKey);
    }

    /**
     * Remove {@code DelayActor} form specific queue and execute
     * {@code DelayActor}
     *
     * @param graphType
     * @param fromNodeById
     * @param toNodeById
     */
    public void removeAndExecuteFirstInQueue(GraphType graphType, long fromNodeById, long toNodeById) {
        GraphTypeFromToNodeKey key = createGraphTypeFromToNodeKey(graphType, fromNodeById,
                toNodeById);

        InnerQueueAndItems innerQueueAndItems = createInnerQueueAndItems(key);
        DelayingSegment queueItems = innerQueueAndItems.queueItems;

        queueItems.executeFirstDelayActorInWaitingQueue();

        setDatatToInnerQueueToQueue(key, queueItems, innerQueueAndItems.innerQueues);

    }

    /**
     * Check possibility to add next {@code DelayActor} to specific queue, if it
     * is possible, then execute {@code DelayActor} and reverse place
     * {@code DelayActor} in new queue.
     *
     * @param graphType
     * @param fromNodeById
     * @param toNodeById
     * @param nextToNodeById
     * @return
     */
    public FromToPositionKey checkAddingToNextQueueAndTakePlaceAndExecuteIfCan(GraphType graphType,
                                                                               long fromNodeById, long toNodeById, long nextToNodeById) {

        GraphTypeFromToNodeKey secondSetionKey = createGraphTypeFromToNodeKey(graphType,
                toNodeById, nextToNodeById);
        InnerQueueAndItems secondInnerQueueAndItems = createInnerQueueAndItems(secondSetionKey);
        DelayingSegment secondQueueItems = secondInnerQueueAndItems.queueItems;

        if (secondQueueItems.hasFreeSpace() == false) {

            DelayActor secondDelayActor = secondQueueItems.showFirstDelayActor();
            if (secondDelayActor == null) {
                return new FromToPositionKey(toNodeById, nextToNodeById);
            } else {
                Long nextDestOfSecondDelayActor = secondDelayActor.nextDestinationByNodeId();

                if ((nextDestOfSecondDelayActor != null && fromNodeById == nextToNodeById && toNodeById == nextDestOfSecondDelayActor) == false) {
                    return new FromToPositionKey(toNodeById, nextToNodeById);
                }
            }

        }

        GraphTypeFromToNodeKey firstSetionKey = createGraphTypeFromToNodeKey(graphType,
                fromNodeById, toNodeById);
        InnerQueueAndItems firstInnerQueueAndItems = createInnerQueueAndItems(firstSetionKey);
        DelayingSegment firstQueueItems = firstInnerQueueAndItems.queueItems;

        DelayActor delayActor = firstQueueItems.showFirstDelayActor();
        secondQueueItems.takePlaceForDelayActor(delayActor.delayActorId(), delayActor.takenSpace());

        setDatatToInnerQueueToQueue(secondSetionKey, secondQueueItems,
                secondInnerQueueAndItems.innerQueues);

        firstQueueItems.executeFirstDelayActorInWaitingQueue();

        setDatatToInnerQueueToQueue(firstSetionKey, firstQueueItems,
                firstInnerQueueAndItems.innerQueues);

        return null;

    }

    private void setDatatToInnerQueueToQueue(GraphTypeFromToNodeKey key,
                                             DelayingSegment queueItems, Map<GraphTypeAndFromToNodeKey, DelayingSegment> innerQueues) {

        innerQueues.put(key.graphTypeAndFromToNodeKey, queueItems);
        queues.put(key.graphTypeAndToNodeKey, innerQueues);

    }

    private GraphTypeFromToNodeKey createGraphTypeFromToNodeKey(GraphType graphType,
                                                                long fromNodeById, long toNodeById) {
        GraphTypeAndToNodeKey graphTypeAndToNodeKey = new GraphTypeAndToNodeKey(graphType,
                toNodeById);
        GraphTypeAndFromToNodeKey graphTypeAndFromToNodeKey = new GraphTypeAndFromToNodeKey(
                graphType, fromNodeById, toNodeById);

        return new GraphTypeFromToNodeKey(graphTypeAndToNodeKey, graphTypeAndFromToNodeKey);
    }

    private InnerQueueAndItems createInnerQueueAndItems(GraphTypeFromToNodeKey key) {
        Map<GraphTypeAndFromToNodeKey, DelayingSegment> innerQueues = queues
                .get(key.graphTypeAndToNodeKey);
        DelayingSegment queueItems = innerQueues.get(key.graphTypeAndFromToNodeKey);
        return new InnerQueueAndItems(innerQueues, queueItems);
    }

    private static class GraphTypeFromToNodeKey {

        public final GraphTypeAndToNodeKey graphTypeAndToNodeKey;
        public final GraphTypeAndFromToNodeKey graphTypeAndFromToNodeKey;

        public GraphTypeFromToNodeKey(GraphTypeAndToNodeKey graphTypeAndToNodeKey,
                                      GraphTypeAndFromToNodeKey graphTypeAndFromToNodeKey) {
            super();
            this.graphTypeAndToNodeKey = graphTypeAndToNodeKey;
            this.graphTypeAndFromToNodeKey = graphTypeAndFromToNodeKey;
        }

    }

    private static class InnerQueueAndItems {

        public final Map<GraphTypeAndFromToNodeKey, DelayingSegment> innerQueues;
        public final DelayingSegment queueItems;

        public InnerQueueAndItems(Map<GraphTypeAndFromToNodeKey, DelayingSegment> innerQueues,
                                  DelayingSegment queueItems) {
            super();
            this.innerQueues = innerQueues;
            this.queueItems = queueItems;
        }

    }

    /**
     * Moves {@code DelayActor} between in and out queues
     *
     * @param fromByNdoeId
     * @param toByNodeId
     * @param graphType
     * @return
     */
    public ResultOfMovingFromDelayToWaitingQueue moveFromInToOutQueueAndReturnQueueDelay(
            long fromByNdoeId, long toByNodeId, GraphType graphType) {

        GraphTypeFromToNodeKey key = createGraphTypeFromToNodeKey(graphType, fromByNdoeId,
                toByNodeId);

        InnerQueueAndItems innerQueueAndItems = createInnerQueueAndItems(key);
        DelayingSegment queueItems = innerQueueAndItems.queueItems;

        if (queueItems.isDelayQueueEmpty()) {
            return new ResultOfMovingFromDelayToWaitingQueue(false);
        }

        long queueDelay = queueItems.moveFromDealyQueueIntoWaitingQueueAndReturnDelayTime();

        setDatatToInnerQueueToQueue(key, queueItems, innerQueueAndItems.innerQueues);

        return new ResultOfMovingFromDelayToWaitingQueue(true, queueDelay);
    }

}
