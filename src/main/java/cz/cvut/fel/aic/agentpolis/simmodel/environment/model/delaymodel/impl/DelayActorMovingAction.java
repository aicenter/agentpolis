package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel.impl;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;

/**
 * Represents {@code DelayActor}, which has not future step.
 * 
 * @author Zbynek Moler
 *
 */
public class DelayActorMovingAction extends ADelayActorMovingAction {

    public DelayActorMovingAction(MovingAction<?> movingAction,
            MovingActionCallback movingActionCallback, long fromPrevPositionByNodeId,
            long currentPositionByNodeId) {
        super(movingAction, movingActionCallback, fromPrevPositionByNodeId, currentPositionByNodeId);
    }

    @Override
    public Long nextDestinationByNodeId() {
        return null;
    }

}
