package cz.agents.agentpolis.simmodel.environment.model.delaymodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import cz.agents.agentpolis.simmodel.environment.model.delaymodel.factory.DelayingSegmentFactory;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndToNodeKey;

public class QueueMock {

	/**
	 *      3    6
	 *      |	 |
	 * 1 -- 2 -- 5 -- 8 -- > 9  -- > 10
	 * 		|    |           ^        |
	 * 		4    7           |        v 
	 *                       12 < -- 11
	 *                         
	 * Segment capacity 1000m
	 */
	
	
	public Map<GraphTypeAndToNodeKey,Map<GraphTypeAndFromToNodeKey,DelayingSegment>> queues = new HashMap<GraphTypeAndToNodeKey,Map<GraphTypeAndFromToNodeKey,DelayingSegment>>();
	
	public final Queue<DelayActor> inQueueF1t2KT = new LinkedList<DelayActor>();
	public final Queue<DelayActor> outQueueF1t2KT = new LinkedList<DelayActor>();
	public Set<String> queueEntityWhichReservedCapacityF1t2KT = new HashSet<String>();

	public final Queue<DelayActor> inQueueF2t4KT = new LinkedList<DelayActor>();
	public final Queue<DelayActor> outQueueF2t4KT = new LinkedList<DelayActor>();
	public Set<String> queueEntityWhichReservedCapacityF2t4KT = new HashSet<String>();
	
	
	public QueueMock(){
		
		DelayingSegmentFactory queueItemsFacotry = new DelayingSegmentFactory();
		
		// --- TRAM 
		
		GraphTypeAndToNodeKey t1T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 1);
		GraphTypeAndFromToNodeKey f2t1KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,2, 1);
		Map<GraphTypeAndFromToNodeKey,DelayingSegment>  innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t1KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		queues.put(t1T, innerQueue);
		
		GraphTypeAndToNodeKey t2T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 2);		
		GraphTypeAndFromToNodeKey f1t2KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,1, 2);	
		GraphTypeAndFromToNodeKey f3t2KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,3, 2);
		GraphTypeAndFromToNodeKey f4t2KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,4, 2);
		GraphTypeAndFromToNodeKey f5t2KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,5, 2);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f1t2KT, new DelayingSegment(1000, inQueueF1t2KT, outQueueF1t2KT, queueEntityWhichReservedCapacityF1t2KT));
		innerQueue.put(f3t2KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f4t2KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f5t2KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		queues.put(t2T, innerQueue);		
		
		GraphTypeAndToNodeKey t3T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 3);			
		GraphTypeAndFromToNodeKey f2t3KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,2, 3);		
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t3KT, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t3T, innerQueue);
		
		GraphTypeAndToNodeKey t4T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 4);
		GraphTypeAndFromToNodeKey f2t4KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,2, 4);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t4KT, new DelayingSegment(1000, inQueueF2t4KT, outQueueF2t4KT, queueEntityWhichReservedCapacityF2t4KT));		
		queues.put(t4T, innerQueue);
				
		GraphTypeAndToNodeKey t5T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 5);	
		GraphTypeAndFromToNodeKey f2t5KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,2, 5);
		GraphTypeAndFromToNodeKey f6t5KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,6, 5);
		GraphTypeAndFromToNodeKey f7t5KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,7, 5);
		GraphTypeAndFromToNodeKey f8t5KT = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM, 8,5);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t5KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f6t5KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f7t5KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f8t5KT, queueItemsFacotry.createDelayingSegmentInstance(1000));
		queues.put(t5T, innerQueue);
		
		GraphTypeAndToNodeKey t6T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 6);
		GraphTypeAndFromToNodeKey f5t6T = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,5, 6);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t6T, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t6T, innerQueue);
		
		GraphTypeAndToNodeKey t7T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 7);	
		GraphTypeAndFromToNodeKey f5t7T = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM, 5,7);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t7T, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t7T, innerQueue);
		
		GraphTypeAndToNodeKey t8T = new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 8);
		GraphTypeAndFromToNodeKey f5t8T = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM, 5,8);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t8T, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t8T, innerQueue);
		
		// -- CAR
		
		GraphTypeAndToNodeKey t1C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 1);	
		GraphTypeAndFromToNodeKey f2t1KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 2,1);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t1KC, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t1C, innerQueue);
		
		GraphTypeAndToNodeKey t2C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 2);
		GraphTypeAndFromToNodeKey f1t2KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,1, 2);
		GraphTypeAndFromToNodeKey f3t2KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 3,2);		
		GraphTypeAndFromToNodeKey f4t2KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,4, 2);
		GraphTypeAndFromToNodeKey f5t2KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 5,2);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f1t2KC, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		innerQueue.put(f3t2KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f4t2KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f5t2KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		queues.put(t2C, innerQueue);
		
		GraphTypeAndToNodeKey t3C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 3);	
		GraphTypeAndFromToNodeKey f2t3KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 2,3);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t3KC, queueItemsFacotry.createDelayingSegmentInstance(1000));				
		queues.put(t3C, innerQueue);
		
		
		GraphTypeAndToNodeKey t4C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 4);
		GraphTypeAndFromToNodeKey f2t4KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,2, 4);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t4KC, queueItemsFacotry.createDelayingSegmentInstance(1000));				
		queues.put(t4C, innerQueue);
		
		
		GraphTypeAndToNodeKey t5C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 5);	
		GraphTypeAndFromToNodeKey f2t5KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 2,5);
		GraphTypeAndFromToNodeKey f6t5KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 6,5);
		GraphTypeAndFromToNodeKey f7t5KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 7,5);
		GraphTypeAndFromToNodeKey f8t5KC = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 8,5);		
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f2t5KC, queueItemsFacotry.createDelayingSegmentInstance(1000));				
		innerQueue.put(f6t5KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f7t5KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		innerQueue.put(f8t5KC, queueItemsFacotry.createDelayingSegmentInstance(1000));
		queues.put(t5C, innerQueue);
		
		GraphTypeAndToNodeKey t6C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 6);
		GraphTypeAndFromToNodeKey f5t6C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 5,6);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t6C, queueItemsFacotry.createDelayingSegmentInstance(1000));						
		queues.put(t6C, innerQueue);
		
		GraphTypeAndToNodeKey t7C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 7);	
		GraphTypeAndFromToNodeKey f5t7C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR, 5,7);
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t7C, queueItemsFacotry.createDelayingSegmentInstance(1000));						
		queues.put(t7C, innerQueue);
		
		GraphTypeAndToNodeKey t8C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 8);
		GraphTypeAndFromToNodeKey f5t8C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,5, 8);		
		innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();
		innerQueue.put(f5t8C, queueItemsFacotry.createDelayingSegmentInstance(1000));		
		queues.put(t8C, innerQueue);
		
		GraphTypeAndToNodeKey t9C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 9);        
        GraphTypeAndFromToNodeKey f8t9C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,8, 9);
        GraphTypeAndFromToNodeKey f12t9C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,12,9);
        innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();        
        innerQueue.put(f8t9C, queueItemsFacotry.createDelayingSegmentInstance(1000));
        innerQueue.put(f12t9C, queueItemsFacotry.createDelayingSegmentInstance(1000));
        queues.put(t9C, innerQueue);
		
        GraphTypeAndToNodeKey t10C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 10);        
        GraphTypeAndFromToNodeKey f9t10C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,9,10);
        innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();        
        innerQueue.put(f9t10C, queueItemsFacotry.createDelayingSegmentInstance(1000));
        queues.put(t10C, innerQueue);

        GraphTypeAndToNodeKey t11C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 11);        
        GraphTypeAndFromToNodeKey f10t11C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,10,11);
        innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();        
        innerQueue.put(f10t11C, queueItemsFacotry.createDelayingSegmentInstance(1000));
        queues.put(t11C, innerQueue);
        
        GraphTypeAndToNodeKey t12C = new GraphTypeAndToNodeKey(EQueueMockGraphType.CAR, 12);        
        GraphTypeAndFromToNodeKey f11t12C = new GraphTypeAndFromToNodeKey(EQueueMockGraphType.CAR,11,12);
        innerQueue = new HashMap<GraphTypeAndFromToNodeKey, DelayingSegment>();        
        innerQueue.put(f11t12C, queueItemsFacotry.createDelayingSegmentInstance(1000));
        queues.put(t12C, innerQueue);
		
	}

}
