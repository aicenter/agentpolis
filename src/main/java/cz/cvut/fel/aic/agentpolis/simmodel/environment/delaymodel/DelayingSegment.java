package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel;

import java.util.Queue;
import java.util.Set;

/**
 * Represents delaying segment between some two position (nodes). This class
 * contains two - java queue - delay and waiting queue. DelayQueue contains
 * delay actors, which wait for move delay actor before with queue delay.
 * WaitingQueue contains delay actors, which first item in queue waits for
 * possible to move to other DelayingSegment if has free space based on future
 * delay actor step. DelayActor has to reserve capacity in future
 * DelayingSegment, if going from other DelayingSegment.
 * 
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class DelayingSegment {

    private final Queue<DelayActor> delayQueue;
    private final Queue<DelayActor> waitingQueue;
    private Set<String> delayAcotrWhichReservedSpace;

    private final double maxSpace;

    private double takenSpace = 0;

    public DelayingSegment(double maxSpace, Queue<DelayActor> delayQueue,
            Queue<DelayActor> waitingQueue, Set<String> delayAcotrWhichReservedSpace) {

        this.delayQueue = delayQueue;
        this.waitingQueue = waitingQueue;
        this.delayAcotrWhichReservedSpace = delayAcotrWhichReservedSpace;
        this.maxSpace = maxSpace;
    }

    /**
     * Adds {@code DelayActor} into {@code DelayingSegment}
     * 
     * @param delayActor
     */
    public void addDelayActor(DelayActor delayActor) {
        incTakenSpace(delayActor.delayActorId(), delayActor.takenSpace());
        delayQueue.add(delayActor);
    }

    /**
     * Provides the first {@code DelayActor} in {@code DelayingSegment}
     * 
     * 
     */
    public DelayActor showFirstDelayActor() {
        return waitingQueue.peek();
    }

    /**
     * Polls the first {@code DelayActor} from {@code DelayingSegment} and
     * updates information inside of {@code DelayingSegment}. At the end of this
     * process it is executed {@code DelayActor} movement.
     * 
     */
    public void executeFirstDelayActorInWaitingQueue() {
        DelayActor delayActor = waitingQueue.poll();
        String delayActorId = delayActor.delayActorId();

        takenSpace = takenSpace - delayActor.takenSpace();
        delayAcotrWhichReservedSpace.remove(delayActorId);

        delayActor.execute();
    }

    /**
     * Returns true if {@code DelayingSegment} contains a some free space for
     * new {@code DelayActor}
     * 
     * @return
     */
    public boolean hasFreeSpace() {
        return takenSpace < maxSpace;
    }

    /**
     * 
     * Make a space reservation for new {@code DelayActor}
     * 
     * @param delayActorId
     * @param delayActorTakenCapacity
     */
    public void takePlaceForDelayActor(String delayActorId, double delayActorTakenCapacity) {

        // assert queueEntityWhichReservedCapacityInQueue.get(queueEntityId) ==
        // null: "Exist queue id in taken place";

        incTakenSpace(delayActorId, delayActorTakenCapacity);
    }

    private void incTakenSpace(String queueEntityId, double entityTakenCapacity) {

        if (delayAcotrWhichReservedSpace.contains(queueEntityId) == false) {
            takenSpace = takenSpace + entityTakenCapacity;
            delayAcotrWhichReservedSpace.add(queueEntityId);
        }
    }

    /**
     * 
     * @return - value is true, if out-queue is empty.
     */
    public boolean isWaitingQueueEmpty() {
        return waitingQueue.isEmpty();
    }

    /**
     * 
     * @return - value is true, if in-queue is empty.
     */
    public boolean isDelayQueueEmpty() {
        return delayQueue.isEmpty();
    }

    /**
     * Moves {@code DelayActor} from delay queue inside {@code DelayingSegment}
     * to the waiting queue. It means that {@code DelayActor} was delayed by
     * {@code DelayActor}s before him and now {@code DelayActor} will be waiting
     * for the next movement into next {@code DelayingSegment}.
     * 
     * @return - the delay time which determines next calling this method for
     *         next movement from delay to waiting queue
     */
    public long moveFromDealyQueueIntoWaitingQueueAndReturnDelayTime() {

        DelayActor delayActor = delayQueue.poll();

        long delayTime = delayActor.delayTime();
        waitingQueue.add(delayActor);

        assert delayTime > 0 : "Queue delay has to be greater then 0. It has to be at least 1";

        return delayTime;
    }

}
