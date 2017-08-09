package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayActor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayingSegment;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.JunctionHandler;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.dto.JunctionHandlingRusult;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.FromToPositionKey;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.GraphTypeAndFromToNodeKey;

/***
 * Implementation  of {@ JunctionHandler} solves situation on junction and moving delay actors.
 * Current implementation iterates for queue directed to crossroad (to crossroadByNodeId) and for each queue move all possibles
 * queue items to other queues, which associate with crossroads. Informs, which queues have free space.
 *  
 * 
 * @author Zbynek Moler
 *
 */
public class JunctionHandlerImpl implements JunctionHandler {

	
	@Override
	public JunctionHandlingRusult handleJunction(long crossroadByNodeId, GraphType graphType, DelayModelStorage queueStorage) {
		 Map<GraphTypeAndFromToNodeKey,DelayingSegment> queueItems = queueStorage.getQueueItems(graphType, crossroadByNodeId);
		 
		 Set<FromToPositionKey> releasedDirectionToCrossroad = new HashSet<FromToPositionKey>();
		 Set<FromToPositionKey> waitingForReleasingDirection = new HashSet<FromToPositionKey>();
		 		 
		 
		 for(GraphTypeAndFromToNodeKey graphTypeAndFromToNodeKey: queueItems.keySet()){
			 long fromByNodeId = graphTypeAndFromToNodeKey.fromNodeId;
			 DelayingSegment items = queueItems.get(graphTypeAndFromToNodeKey);
			 
			 FromToPositionKey fromToPositionKey = new FromToPositionKey(fromByNodeId, crossroadByNodeId);
			 
			 while(items.isWaitingQueueEmpty() == false){
			 
				 DelayActor delayActor = items.showFirstDelayActor();

				 if(delayActor.nextDestinationByNodeId() != null){				 
					 FromToPositionKey waitingFromTo = queueStorage.checkAddingToNextQueueAndTakePlaceAndExecuteIfCan(graphType, fromByNodeId, crossroadByNodeId, delayActor.nextDestinationByNodeId());
					 if(waitingFromTo != null){		
						waitingForReleasingDirection.add(waitingFromTo);
						break;
					 }				 
				 }else{
					 queueStorage.removeAndExecuteFirstInQueue(graphType, fromByNodeId, crossroadByNodeId);				 
				 }		
			
			}
			 
			 if(items.hasFreeSpace()){
			 	releasedDirectionToCrossroad.add(fromToPositionKey);
			 }
			 			 			  			 			 
			 
		 }
		  
		 return new JunctionHandlingRusult(releasedDirectionToCrossroad, waitingForReleasingDirection);
	}

}
