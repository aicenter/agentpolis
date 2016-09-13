package cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayActor;

/**
 * Class implements part methods from {@code DelayActor}, which are the same for
 * all delay actors.
 * 
 * @author Zbynek Moler
 *
 */
public abstract class ADelayActorMovingAction implements DelayActor {

    private final MovingAction<?> movingAction;
    private final MovingActionCallback movingActionCallback;
    private final long fromPrevPositionByNodeId;
    private final long currentPositionByNodeId;

    public ADelayActorMovingAction(MovingAction<?> movingAction,
            MovingActionCallback movingActionCallback, long fromPrevPositionByNodeId,
            long currentPositionByNodeId) {
        super();
        this.movingActionCallback = movingActionCallback;
        this.movingAction = movingAction;
        this.fromPrevPositionByNodeId = fromPrevPositionByNodeId;
        this.currentPositionByNodeId = currentPositionByNodeId;
    }

    @Override
    public void execute() {
        movingActionCallback.nextMove(fromPrevPositionByNodeId, currentPositionByNodeId);
    }

    @Override
    public long delayTime() {
        return movingAction.moveTime();
    }

    @Override
    public String delayActorId() {
        return movingAction.movingEntityId();
    }

    @Override
    public double takenSpace() {
        return movingAction.takenCapacity();
    }

}
