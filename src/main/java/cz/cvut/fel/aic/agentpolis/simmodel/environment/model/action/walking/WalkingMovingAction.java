package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.walking;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * Class is implementation of {@ MovingAction} and represents specific type on movement 
 * activity - walking.
 * 
 * @author Zbynek Moler
 *
 */
public class WalkingMovingAction implements MovingAction<TripItem> {

	private final WalkingAction walkingAction;
	private final long moveTime;
	private final String walkingId;
	private final double walkerStepLength;

	public WalkingMovingAction(final WalkingAction walkingAction, final long moveTime, final String walkingId,
			final double walkerStepLength) {
		this.walkingAction = walkingAction;
		this.moveTime = moveTime;
		this.walkingId = walkingId;
		this.walkerStepLength = walkerStepLength;
	}

	/**
	 * Method moves walker to next position
	 */
	@Override
	public void moveToNextNode(int startNode, int destinationByNodeId, GraphType typeOfGraphForMoving) {
		walkingAction.walk(walkingId,startNode, destinationByNodeId, typeOfGraphForMoving);

	}

	/**
	 * Walker duration for one step (width of walker (person width in meter)/ walker velocity)
	 */
	@Override
	public long moveTime() {
		return moveTime;
	}

	/**
	 * It calls back immediately - because nobody does not need to next walker (agent) position 
	 */
	@Override
	public void notifyPlanForNextMove(int startNode, int destinationByNodeId,
									  MovingActionCallback movingActionCallback) {

		movingActionCallback.dependentEntitiesWereNotifiedAboutMovingPlan(startNode, destinationByNodeId);

	}
	
	
	/**
	 * Returns walker id = agent id 
	 */
	@Override
	public String movingEntityId() {
		return walkingId;
	}

	/**
	 * Returns width of walker (person width in meter)  
	 */
	@Override
	public double takenCapacity() {
		return walkerStepLength;
	}

	/**
	 * It calls back immediately - because nobody depends on walking 
	 */
	@Override
	public void beforeNotifyingAboutPlan(MovingActionCallback movingActionCallback, long fromPositionByNodeId, long toPositionByNodeId){	
		movingActionCallback.endBeforeNotifyingAboutPlan();
		
	}

	/**
	 * It calls back immediately - because agents does not need to wait for next step
	 */
	@Override
	public void waitToDepartureTime(TripItem fromTripItem, TripItem toTripItem,
			MovingActionCallback movingActionCallback) {
		movingActionCallback.endWaitingToDepartureTime(fromTripItem.tripPositionByNodeId, toTripItem.tripPositionByNodeId);

		
	}

}
