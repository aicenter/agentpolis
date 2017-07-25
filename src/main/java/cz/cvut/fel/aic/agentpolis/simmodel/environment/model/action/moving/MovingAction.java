package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * Methods for moving entity (driving , on foot) 
 * 
 * @author Zbynek Moler
 *
 */
public interface MovingAction<TTripItem extends TripItem> {

	/**
	 * Notifies some entities, for which are these information important to know. 
	 * 
	 * It must be to invoke before {@ moveToNextNode} and  {@ waitToDepartureTime}
	 * 
	 * @param startNode
	 * @param destinationByNodeId
	 * @param movingActionCallback
	 */
	public void notifyPlanForNextMove(int startNode, int destinationByNodeId, MovingActionCallback movingActionCallback);
	
	public void beforeNotifyingAboutPlan(MovingActionCallback movingActionCallback, long fromPositionByNodeId, long toPositionByNodeId);
	
	/**
	 * 
	 * Moves entity (entities - linked entities) to next place (node)
	 *  
	 * 
	 * @param startNode
	 * @param destinationByNodeId
	 * @param typeOfGraphForMoving
	 */
	public void moveToNextNode(int startNode, int destinationByNodeId, GraphType typeOfGraphForMoving);
	
	/**
	 * Average time for move to next place (node) 
	 * Move time has to be grater then 0 
	 * 
	 * @return
	 */
	public long moveTime();
	
	/**
	 * Unique id, which represents moving entity - e.g. the same as agent or vehicle
	 * @return
	 */	
	public String movingEntityId(); 

	/**
	 * Capacity which moving object to take e.g. on route 
	 * @return
	 */
	public double takenCapacity();
	
	/**
	 * Waiting entity to departure time
	 * 
	 * It must be to invoke before {@ moveToNextNode}
	 * 
	 * @param fromNode
	 * @param toNode
	 * @param movingActionCallback
	 */
	public void waitToDepartureTime(TTripItem fromTripItem,TTripItem toTripItem, MovingActionCallback movingActionCallback);
	
	
	
	
}
