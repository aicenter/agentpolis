package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel.impl;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;

/**
 * Represents {@code DelayActor}, which has future step.
 * 
 * @author Zbynek Moler
 *
 */

public class DelayActorWithNextDestMovingAction extends ADelayActorMovingAction {

    private final long nextDestByNodeId;

    public DelayActorWithNextDestMovingAction(MovingAction<?> movingAction,
            MovingActionCallback movingActionCallback, long fromPrevPositionByNodeId,
            long currentPositionByNodeId, long nextDestByNodeId) {
        super(movingAction, movingActionCallback, fromPrevPositionByNodeId, currentPositionByNodeId);

        this.nextDestByNodeId = nextDestByNodeId;
    }

    @Override
    public Long nextDestinationByNodeId() {
        return nextDestByNodeId;
    }

}
