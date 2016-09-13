package cz.agents.agentpolis.simmodel.environment.model.delaymodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorWithNextDestMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelStorage;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.FromToPositionKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndToNodeKey;

public class QueueStorage2Test {

	
	QueueMock queueMock;
	
	
	@Before
	public void setUp(){
		this.queueMock = new QueueMock();
		
	}
	
	
	

 	

	
	@Test
	public void queueStorageTest(){
	
	    MovingAction<?> mf1t2 = mock(MovingAction.class);
		when(mf1t2.takenCapacity()).thenReturn(500.0);
		when(mf1t2.movingEntityId()).thenReturn("mf1t2");

		
		DelayActor queueItemF1T2T = new DelayActorMovingAction(mf1t2, mock(MovingActionCallback.class),1, 2);		
		DelayModelStorage queueStorage = new DelayModelStorage(queueMock.queues); 
		queueStorage.addQueueItems(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);
		
		assertEquals(1, queueMock.inQueueF1t2KT.size());
		assertEquals(0, queueMock.outQueueF1t2KT.size());
		assertEquals(0, queueMock.inQueueF2t4KT.size());
		assertEquals(0, queueMock.inQueueF2t4KT.size());
		
		
	}
	
	@Test
	public void queueStorageTest2(){
	
		DelayModelStorage queueStorage = new DelayModelStorage(queueMock.queues);
		
		MovingAction<?> mf1t2 = mock(MovingAction.class);
		when(mf1t2.takenCapacity()).thenReturn(500.0);
		when(mf1t2.movingEntityId()).thenReturn("mf1t2");
		when(mf1t2.moveTime()).thenReturn((long) 1);
	 
		DelayActor queueItemF1T2T = new DelayActorWithNextDestMovingAction(mf1t2, mock(MovingActionCallback.class),1, 2, 4);				
		queueStorage.addQueueItems(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);

		MovingAction<?> mf2t4f = mock(MovingAction.class);
		when(mf2t4f.takenCapacity()).thenReturn(500.0);
		when(mf2t4f.movingEntityId()).thenReturn("mf2t4f");
		when(mf2t4f.moveTime()).thenReturn((long)1);
		
		DelayActor queueItemF2T4Tf = new DelayActorMovingAction(mf2t4f, mock(MovingActionCallback.class),2, 4);				
		queueStorage.addQueueItems(2, 4, EQueueMockGraphType.TRAM, queueItemF2T4Tf);
		
		MovingAction<?> mf2t4s = mock(MovingAction.class);
		when(mf2t4s.takenCapacity()).thenReturn(500.0);
		when(mf2t4s.movingEntityId()).thenReturn("mf2t4s");
		when(mf2t4s.moveTime()).thenReturn((long)1);

		DelayActor queueItemF2T4Ts = new DelayActorMovingAction(mf2t4s, mock(MovingActionCallback.class), 2,4);						
		queueStorage.addQueueItems(2, 4, EQueueMockGraphType.TRAM, queueItemF2T4Ts);
		
		
		assertFalse(queueMock.inQueueF1t2KT.isEmpty());
		assertTrue(queueMock.outQueueF1t2KT.isEmpty());
		
		Map<GraphTypeAndFromToNodeKey,DelayingSegment> queueItems = queueMock.queues.get(new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 2));		
		DelayingSegment item = queueItems.get(new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,1, 2));				
		item.moveFromDealyQueueIntoWaitingQueueAndReturnDelayTime();

		assertTrue(queueMock.inQueueF1t2KT.isEmpty());
		assertFalse(queueMock.outQueueF1t2KT.isEmpty());
		
		assertFalse(queueMock.inQueueF2t4KT.isEmpty());
		assertTrue(queueMock.outQueueF2t4KT.isEmpty());
				
		queueItems = queueMock.queues.get(new GraphTypeAndToNodeKey(EQueueMockGraphType.TRAM, 4));
		item = queueItems.get(new GraphTypeAndFromToNodeKey(EQueueMockGraphType.TRAM,2, 4));
		item.moveFromDealyQueueIntoWaitingQueueAndReturnDelayTime();
		
		
		FromToPositionKey regPlace = queueStorage.checkAddingToNextQueueAndTakePlaceAndExecuteIfCan(EQueueMockGraphType.TRAM, 1, 2, queueItemF1T2T.nextDestinationByNodeId());		
		assertNotNull(regPlace);				

		
		
	}
	


}
