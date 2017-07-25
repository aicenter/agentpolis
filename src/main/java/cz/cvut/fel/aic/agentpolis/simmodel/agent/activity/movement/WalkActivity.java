package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.WalkingActivityLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.WalkTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback.MovementActivityCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback.WalkingActivityCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.util.MoveTimeNormalizer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.walking.WalkingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.walking.WalkingMovingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.entityvelocitymodel.query.AgentInfoQuery;

/**
 * Via this activity could be executed walking by an agent
 * 
 * @author Zbynek Moler
 * 
 */
public class WalkActivity implements MovementActivityCallback {

    private final int AVG_LENGTH_OF_AGENT_STEP = 1; // in meter

    private final WalkingAction walkingAction;
    private final AgentInfoQuery agentInfoSensor;
    private final MovementActivityTripItem movementActivity;

    private final WalkingActivityLogger walkingActivityLogger;

    private WalkingActivityCallback walkingActiovityCallback = null;
    private String agentId = null;

    @Inject
    public WalkActivity(WalkingAction walkingAction, AgentInfoQuery agentInfoSensor,
            MovementActivityTripItem movementActivity, WalkingActivityLogger walkingActivityLogger) {
        this.walkingAction = walkingAction;
        this.agentInfoSensor = agentInfoSensor;
        this.movementActivity = movementActivity;
        this.walkingActivityLogger = walkingActivityLogger;
    }

    /**
     * Execute walk base on trip.
     */
    public void walk(final String agentId, final WalkTrip trip,
            final WalkingActivityCallback walkingActiovityCallback) {

        this.walkingActiovityCallback = walkingActiovityCallback;
        this.agentId = agentId;

        walkingActivityLogger.logStartWalking(agentId);

        long moveTime = computeMoveTime(agentId);
        movementActivity.move(agentId, this, new WalkingMovingAction(walkingAction, moveTime,
                agentId, AVG_LENGTH_OF_AGENT_STEP), trip);
    }

    private long computeMoveTime(String agentId) {
        double agentVelocity = agentInfoSensor.getCurrrentAgentVelocity(agentId);
        long moveTime = (long) (AVG_LENGTH_OF_AGENT_STEP / agentVelocity);

        return MoveTimeNormalizer.normalizeMoveTimeForQueue(moveTime);
    }

    /**
     * After agent will finish walking movement based on trip, then this method
     * is invoked.
     */
    public void finishedMovement() {

        walkingActivityLogger.logEndWalking(agentId);

        walkingActiovityCallback.finishedWalking();

        this.agentId = null;
        this.walkingActiovityCallback = null;

    }

}
