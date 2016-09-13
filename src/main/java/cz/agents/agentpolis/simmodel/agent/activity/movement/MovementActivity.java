package cz.agents.agentpolis.simmodel.agent.activity.movement;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.MovementActivityLogger;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.MovementActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.AgentPositionAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.action.DelayAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorWithNextDestMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;

/**
 * The abstract representation for a movement - using for driving, walking
 * activity
 *
 * @author Zbynek Moler
 */
public abstract class MovementActivity<TTripItem extends TripItem> implements PositionUpdated,
        MovingActionCallback {

    private static final Logger LOGGER = Logger.getLogger(MovementActivity.class);

    private final AgentPositionAction agentPositionAction;
    private final DelayAction queueAction;
    private final TransportNetworks transportNetworks;
    private final MovementActivityLogger movementActivityLogger;

    private TTripItem lastFromTripItem = null;

    private TTripItem movementCurrentTripItem = null;

    private MovementActivityCallback movementCallback = null;
    private MovingAction<? super TTripItem> movingAction = null;
    private Trip<TTripItem> trip = null;
    private GraphType tripGraphType = null;
    private String agentId = null;

    @Inject
    public MovementActivity(DelayAction queueAction, AgentPositionAction agentPositionAction,
            TransportNetworks transportNetworks, MovementActivityLogger movementActivityLogger) {

        this.queueAction = queueAction;
        this.agentPositionAction = agentPositionAction;
        this.transportNetworks = transportNetworks;
        this.movementActivityLogger = movementActivityLogger;

    }

    /**
     * Start move based on trip
     */
    public void move(String agentId, MovementActivityCallback movementCallback,
            MovingAction<TTripItem> movingAction, Trip<TTripItem> trip) {

        assert this.movementCallback == null && this.movingAction == null && this.trip == null
                && this.tripGraphType == null && this.agentId == null : "Instance of current movement is using";

        this.movementCallback = movementCallback;
        this.movingAction = movingAction;
        this.trip = trip;
        this.lastFromTripItem = null;
        this.agentId = agentId;
        this.tripGraphType = trip.getGraphType();

        agentPositionAction.addSensingPositionNode(agentId, this);
        moveToNextNode(trip.getAndRemoveFirstTripItem());

    }

    private void moveToNextNode(TTripItem fromTripItem) {

        if (!trip.hasNextTripItem()) {
            agentPositionAction.removeSensingPositionNode(agentId, this);
            movementCallback.finishedMovement();
            this.movingAction = null;
            this.trip = null;
            this.tripGraphType = null;
            this.agentId = null;
            this.movementCallback = null;

            return;
        }

        lastFromTripItem = fromTripItem;
        movementCurrentTripItem = trip.getAndRemoveFirstTripItem();

        // LOGGER.debug("Agent " + agentId + " moving from " + fromTripItem +
        // " to " + movementCurrentTripItem);

        if (checkActionFeasibility(lastFromTripItem, movementCurrentTripItem)) {
            if (!agentPositionAction.setTargetPosition(agentId,
                    movementCurrentTripItem.tripPositionByNodeId))
                movingAction.waitToDepartureTime(fromTripItem, movementCurrentTripItem, this);
        }
    }

    /**
     * The method ensure that the agent movement is feasible for execution. If
     * the movement is not feasible then agent will freeze.
     * <p/>
     * This functionality was discussed. The motivation behind is to ensure run
     * simulation even if error is invoked. The potential extension is to add
     * method into MovementActivityCallback, which will inform agent about
     * execution infeasibility.
     *
     * @param from
     * @param to
     * @return
     */
    private boolean checkActionFeasibility(TTripItem from, TTripItem to) {

        Graph<?, ? extends Edge> graph = transportNetworks.getGraph(tripGraphType);
        if (graph == null) {
            LOGGER.error("The agent with id: agentId is not able to execute movement. Agent will freeze on the current possiton. It does not exist  with given graph type"
                    + tripGraphType);
            return false;
        }

        Edge edge = graph.getEdge(from.tripPositionByNodeId, to.tripPositionByNodeId);
        if (edge == null) {
            LOGGER.error("The agent with id: agentId is not able to execute movement. Agent will freeze on the current possiton. It does not exist the edge from - "
                    + from.tripPositionByNodeId + " to - " + to.tripPositionByNodeId);
            return false;
        }

        return true;

    }

    /**
     * This method is invoked just as callback by some sensor. Do not call this
     * directly. The method informs about entity new position.
     */
    @Override
    public void newEntityPosition(String entityId, long currentPositionByNodeId) {

        long lastFromPositionByNodeId = lastFromTripItem.tripPositionByNodeId;

        if (trip.hasNextTripItem()) {
            queueAction.addDelayActor(
                    lastFromPositionByNodeId,
                    currentPositionByNodeId,
                    tripGraphType,
                    new DelayActorWithNextDestMovingAction(movingAction, this,
                            lastFromPositionByNodeId, currentPositionByNodeId, trip
                                    .showCurrentTripItem().tripPositionByNodeId));
        } else {
            queueAction.addDelayActor(lastFromPositionByNodeId, currentPositionByNodeId,
                    tripGraphType, new DelayActorMovingAction(movingAction, this,
                            lastFromPositionByNodeId, currentPositionByNodeId));
        }
    }

    @Override
    public void dependentEntitiesWereNotifiedAboutMovingPlan(int fromByNodeId, int toNodeId) {
        movingAction.moveToNextNode(fromByNodeId, toNodeId, tripGraphType);

    }

    @Override
    public void endWaitingToDepartureTime(int fromByNodeId, int toByNodeId) {
        movingAction.notifyPlanForNextMove(fromByNodeId, toByNodeId, this);

    }

    @Override
    public void nextMove(long fromPrevPositionByNodeId, long currentPositionByNodeId) {
        movingAction.beforeNotifyingAboutPlan(this, fromPrevPositionByNodeId,
                currentPositionByNodeId);
    }

    @Override
    public void endBeforeNotifyingAboutPlan() {

        // ---- log ----
        movementActivityLogger.logMovementArrivalLogItem(agentId,
                lastFromTripItem.tripPositionByNodeId,
                movementCurrentTripItem.tripPositionByNodeId, tripGraphType);
        // ---- log ----

        moveToNextNode(movementCurrentTripItem);

    }

}
