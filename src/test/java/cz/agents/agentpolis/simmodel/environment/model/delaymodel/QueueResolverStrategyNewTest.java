package cz.agents.agentpolis.simmodel.environment.model.delaymodel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.dto.JunctionHandlingRusult;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorWithNextDestMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelStorage;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.JunctionHandlerImpl;

public class QueueResolverStrategyNewTest {

	QueueMock queueMock;
	
	@Before
	public void setUp(){
		this.queueMock = new QueueMock();
		
	}
	
	
	@Test	
	public void queueResolverStrategyNewTest(){
		
		DelayModelStorage queueStorage = new DelayModelStorage(queueMock.queues);
		
		MovingAction<?> mf1t2 = mock(MovingAction.class);
		when(mf1t2.takenCapacity()).thenReturn(500.0);
		when(mf1t2.movingEntityId()).thenReturn("mf1t2");
		when(mf1t2.moveTime()).thenReturn((long)1);
		
		
		DelayActor queueItemF1T2T = new DelayActorWithNextDestMovingAction(mf1t2, mock(MovingActionCallback.class),1, 2, 4);				
		queueStorage.addQueueItems(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);

		MovingAction<?> mf2t4f = mock(MovingAction.class);
		when(mf2t4f.takenCapacity()).thenReturn(500.0);
		when(mf2t4f.movingEntityId()).thenReturn("mf2t4f");
		when(mf2t4f.moveTime()).thenReturn((long)1);
		
		DelayActor queueItemF2T4Tf = new DelayActorMovingAction(mf2t4f, mock(MovingActionCallback.class),2, 4);				
		queueStorage.addQueueItems(2, 4, EQueueMockGraphType.TRAM, queueItemF2T4Tf);
		
		queueStorage.moveFromInToOutQueueAndReturnQueueDelay(1, 2, EQueueMockGraphType.TRAM);
		queueStorage.moveFromInToOutQueueAndReturnQueueDelay(2, 4, EQueueMockGraphType.TRAM);
		
		JunctionHandlerImpl resolverStrategyNewImpl = new JunctionHandlerImpl();
		JunctionHandlingRusult crossroadResolvingResult = resolverStrategyNewImpl.handleJunction(2, EQueueMockGraphType.TRAM, queueStorage);
		
		assertEquals(4, crossroadResolvingResult.releasedDirectionToCrossroad.size());
		assertEquals(0, crossroadResolvingResult.waitingForReleasingDirection.size());
		
		
		
		
	}
	
	@Test	
	public void queueResolverStrategyNewTest2(){
		
		DelayModelStorage queueStorage = new DelayModelStorage(queueMock.queues);
		
		MovingAction<?> mf1t2 = mock(MovingAction.class);
		when(mf1t2.takenCapacity()).thenReturn(500.0);
		when(mf1t2.movingEntityId()).thenReturn("mf1t2");
		when(mf1t2.moveTime()).thenReturn((long)1);
		
		DelayActor queueItemF1T2T = new DelayActorWithNextDestMovingAction(mf1t2, mock(MovingActionCallback.class),1, 2, 4);				
		queueStorage.addQueueItems(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);

		MovingAction<?> mf2t4f = mock(MovingAction.class);
		when(mf2t4f.takenCapacity()).thenReturn(500.0);
		when(mf2t4f.movingEntityId()).thenReturn("mf2t4f");
		when(mf2t4f.moveTime()).thenReturn((long)1);
		
		DelayActor queueItemF2T4Tf = new DelayActorMovingAction(mf2t4f, mock(MovingActionCallback.class), 2,4);				
		queueStorage.addQueueItems(2, 4, EQueueMockGraphType.TRAM, queueItemF2T4Tf);

		
		MovingAction<?> mf2t4fs = mock(MovingAction.class);
		when(mf2t4fs.takenCapacity()).thenReturn(500.0);
		when(mf2t4fs.movingEntityId()).thenReturn("mf2t4fs");

		DelayActor queueItemF2T4Tfs = new DelayActorMovingAction(mf2t4fs, mock(MovingActionCallback.class),2, 4);				
		queueStorage.addQueueItems(2, 4, EQueueMockGraphType.TRAM, queueItemF2T4Tfs);		
		
		queueStorage.moveFromInToOutQueueAndReturnQueueDelay(1, 2, EQueueMockGraphType.TRAM);
		queueStorage.moveFromInToOutQueueAndReturnQueueDelay(2, 4, EQueueMockGraphType.TRAM);
		
		JunctionHandlerImpl resolverStrategyNewImpl = new JunctionHandlerImpl();
		JunctionHandlingRusult crossroadResolvingResult = resolverStrategyNewImpl.handleJunction(2, EQueueMockGraphType.TRAM, queueStorage);
		
		assertEquals(4, crossroadResolvingResult.releasedDirectionToCrossroad.size());
		assertEquals(1, crossroadResolvingResult.waitingForReleasingDirection.size());
		
		
		
		
	}
}
