package cz.agents.agentpolis.simmodel.environment.model.action.moving;

/**
 * Decalration of callback methods for moving action
 * 
 * @author Zbynek Moler
 *
 */
public interface MovingActionCallback {

	/**
	 * Callback method informs, that all dependent entities were notified.  
	 * 
	 * @param fromByNodeId
	 * @param toNodeId
	 */
	public void dependentEntitiesWereNotifiedAboutMovingPlan(int fromByNodeId, int toNodeId);
		
	
	/**
	 * It is invoked, when departure time is equals simulation time 
	 * 
	 * @param fromNode
	 * @param toNode
	 */
	public void endWaitingToDepartureTime(int fromNode, int toNode);
	
	/**
	 * It is invoked, when it is possible to move to next node
	 * 
	 * @param currentPositionByNodeId
	 */
	public void nextMove(long fromPrevPositionByNodeId, long currentPositionByNodeId);			
	
	/**
	 * Callback method informs about end notifying plan.
	 * 
	 */
	public void endBeforeNotifyingAboutPlan();
}
