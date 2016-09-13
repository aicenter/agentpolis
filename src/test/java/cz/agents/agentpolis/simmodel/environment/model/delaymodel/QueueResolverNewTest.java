package cz.agents.agentpolis.simmodel.environment.model.delaymodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayActorMovingAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelImpl;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.JunctionHandlerImpl;
import cz.agents.alite.common.event.EventProcessor;

public class QueueResolverNewTest {

    QueueMock queueMock;

    @Before
    public void setUp() {
        this.queueMock = new QueueMock();

    }

    @Test
    public void queueResolverNewTest() {

        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        MovingActionCallback movingActionCallback = mock(MovingActionCallback.class);

        MovingAction<?> mf1t2 = mock(MovingAction.class);
        when(mf1t2.takenCapacity()).thenReturn(500.0);
        when(mf1t2.movingEntityId()).thenReturn("mf1t2");
        when(mf1t2.moveTime()).thenReturn((long) 1);

        DelayActor queueItemF1T2T = new DelayActorMovingAction(mf1t2, movingActionCallback, 1, 2);

        assertEquals(0, queueMock.inQueueF1t2KT.size());
        assertEquals(0, eventProcessor.getCurrentQueueLength());

        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);

        assertEquals(1, eventProcessor.getCurrentQueueLength());
        assertEquals(1, queueMock.inQueueF1t2KT.size());
        assertEquals(0, queueMock.outQueueF1t2KT.size());

    }

    @Test
    public void queueResolverNewTest2() {

        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        DelayActorMockImpl queueItemF1T2T = new DelayActorMockImpl("mf1t2", 100, 500,eventProcessor);

        assertEquals(0, queueMock.inQueueF1t2KT.size());

        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);

        assertEquals(1, queueMock.inQueueF1t2KT.size());
        assertEquals(0, eventProcessor.getCurrentTime());
        assertFalse(queueItemF1T2T.wasExecuted);

        eventProcessor.run();

        assertEquals(101, eventProcessor.getCurrentTime());

        assertTrue(queueItemF1T2T.wasExecuted);

        assertEquals(0, queueMock.inQueueF1t2KT.size());
        assertEquals(0, queueMock.outQueueF1t2KT.size());

    }

    @Test
    public void queueResolverNewTest3() {

        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        DelayActorMockImpl queueItemF1T2T = new DelayActorMockImpl("mf1t2", 100, 500,eventProcessor);
        DelayActorMockImpl queueItemF1T2TS = new DelayActorMockImpl("mf1t2s", 100, 500,eventProcessor);

        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2TS);

        assertEquals(0, eventProcessor.getCurrentTime());
        assertFalse(queueItemF1T2T.wasExecuted);

        eventProcessor.run();

        
        assertTrue(queueItemF1T2T.wasExecuted);
        assertTrue(queueItemF1T2TS.wasExecuted);

        assertEquals(2, queueItemF1T2T.executionTime);      // No delay, 2 ms is junction handling
        assertEquals(102,queueItemF1T2TS.executionTime);    // Delay 100 ms
        assertEquals(201, eventProcessor.getCurrentTime()); // delay for vehicle, which are behind F1T2TS, delaing start 101 therefore 201 
        
        assertEquals(0, queueMock.inQueueF1t2KT.size());
        assertEquals(0, queueMock.outQueueF1t2KT.size());

    }

    
    
    @Test
    public void deadlockResolving() {        
    
        /*
         * 
         *  DeadLock
         *  
         *  Vehicles go from position 1 to 2.
         *  Vehicle go from position 2 to 1.
         *  
         *  From 1 to 2 = 1000m
         *  From 2 to 1 = 1000m
         *  
         *  Vehicle has 500m :-)
         * 
         */
        
        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        DelayActorMockWithNextDestImpl queueItemF1T2T = new DelayActorMockWithNextDestImpl("mf1t2",
                100, 500,  eventProcessor,1);
        DelayActorMockWithNextDestImpl queueItemF1T2TS = new DelayActorMockWithNextDestImpl(
                "mf1t2s", 100, 500,  eventProcessor, 1);
        DelayActorMockWithNextDestImpl queueItemF2T1T = new DelayActorMockWithNextDestImpl("mf2t1",
                100, 500,  eventProcessor,2);
        DelayActorMockWithNextDestImpl queueItemF2T1TS = new DelayActorMockWithNextDestImpl(
                "mf2t1s", 100, 500, eventProcessor,2);

        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2T);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.TRAM, queueItemF1T2TS);
        queueResolverNew.handleDelayActor(2, 1, EQueueMockGraphType.TRAM, queueItemF2T1T);
        queueResolverNew.handleDelayActor(2, 1, EQueueMockGraphType.TRAM, queueItemF2T1TS);

        assertEquals(0, eventProcessor.getCurrentTime());
        assertFalse(queueItemF1T2T.wasExecuted);

        eventProcessor.run();

        assertTrue(queueItemF1T2T.wasExecuted);
        assertTrue(queueItemF1T2TS.wasExecuted);
        assertTrue(queueItemF2T1T.wasExecuted);
        assertTrue(queueItemF2T1TS.wasExecuted);

        assertTrue(queueItemF1T2T.executionTime < queueItemF1T2TS.executionTime);
        assertTrue(queueItemF2T1T.executionTime < queueItemF2T1TS.executionTime);        

        assertEquals(0, queueMock.inQueueF1t2KT.size());
        assertEquals(0, queueMock.outQueueF1t2KT.size());

    }

    @Test
    public void deadlock() {        
    
        /*
         * 
         *  DeadLock
         *  
         *  Vehicles go from position 9 to 10.
         *  Vehicles go from position 10 to 11.
         *  Vehicles go from position 11 to 12.
         *  Vehicles go from position 12 to 9.
         *  
         *  From 9 to 10 = 1000m
         *  From 10 to 11 = 1000m
         *  From 11 to 12 = 1000m
         *  From 12 to 9 = 1000m
         *  
         *  Vehicle has 500m :-)
         * 
         */
        
        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        DelayActorMockWithNextDestImpl queueItemF9T10T = new DelayActorMockWithNextDestImpl("mf9t10",
                100, 500,  eventProcessor,11);
        DelayActorMockWithNextDestImpl queueItemF9T10TS = new DelayActorMockWithNextDestImpl(
                "mf9t10s", 100, 500,  eventProcessor, 11);
        
        DelayActorMockWithNextDestImpl queueItemF10T11T = new DelayActorMockWithNextDestImpl("mf10t11",
                100, 500,  eventProcessor,12);
        DelayActorMockWithNextDestImpl queueItemF10T11TS = new DelayActorMockWithNextDestImpl(
                "mf10t11s", 100, 500, eventProcessor,12);


        DelayActorMockWithNextDestImpl queueItemF11T12T = new DelayActorMockWithNextDestImpl("mf11t12",
                100, 500,  eventProcessor,9);
        DelayActorMockWithNextDestImpl queueItemF11T12TS = new DelayActorMockWithNextDestImpl(
                "mf11t12s", 100, 500, eventProcessor,9);

        
        DelayActorMockWithNextDestImpl queueItemF12T9T = new DelayActorMockWithNextDestImpl("mf12t9",
                100, 500,  eventProcessor,10);
        DelayActorMockWithNextDestImpl queueItemF12T9TS = new DelayActorMockWithNextDestImpl(
                "mf12t9s", 100, 500, eventProcessor,10);

        
        
        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(9, 10, EQueueMockGraphType.CAR, queueItemF9T10T);
        queueResolverNew.handleDelayActor(9, 10, EQueueMockGraphType.CAR, queueItemF9T10TS);
        queueResolverNew.handleDelayActor(10, 11, EQueueMockGraphType.CAR, queueItemF10T11T);
        queueResolverNew.handleDelayActor(10, 11, EQueueMockGraphType.CAR, queueItemF10T11TS);

        queueResolverNew.handleDelayActor(11, 12, EQueueMockGraphType.CAR, queueItemF11T12T);
        queueResolverNew.handleDelayActor(11, 12, EQueueMockGraphType.CAR, queueItemF11T12TS);
        queueResolverNew.handleDelayActor(12, 9, EQueueMockGraphType.CAR, queueItemF12T9T);
        queueResolverNew.handleDelayActor(12, 9, EQueueMockGraphType.CAR, queueItemF12T9TS);
        
        assertEquals(0, eventProcessor.getCurrentTime());
        assertFalse(queueItemF9T10T.wasExecuted);

        eventProcessor.run();

        assertFalse(queueItemF9T10T.wasExecuted);
        assertFalse(queueItemF9T10TS.wasExecuted);
        assertFalse(queueItemF10T11T.wasExecuted);
        assertFalse(queueItemF10T11TS.wasExecuted);

     
    }

    @Test
    public void deadlock2() {        
    
        /*
         * 
         *  DeadLock
         *  
         *  Vehicles go from position 9 to 10.
         *  Vehicles go from position 10 to 11.
         *  Vehicles go from position 11 to 12.
         *  Vehicles go from position 12 to 9.
         *  
         *  From 9 to 10 = 1000m
         *  From 10 to 11 = 1000m
         *  From 11 to 12 = 1000m
         *  From 12 to 9 = 1000m
         *  
         *  Vehicle has 500m :-)
         * 
         */
        
        EventProcessor eventProcessor = new EventProcessor();
        JunctionHandlerImpl queueResolverStrategy = new JunctionHandlerImpl();

        DelayActorMockWithNextDestImpl queueItemF1T2T = new DelayActorMockWithNextDestImpl("mf1t2",
                100, 500,  eventProcessor,5);
        DelayActorMockWithNextDestImpl queueItemF1T2TS = new DelayActorMockWithNextDestImpl(
                "mf1t2s", 100, 500,  eventProcessor, 5);
        
        DelayActorMockWithNextDestImpl queueItemF2T1T = new DelayActorMockWithNextDestImpl("mf2t1",
                100, 500,  eventProcessor,2);
        DelayActorMockWithNextDestImpl queueItemF2T1TS = new DelayActorMockWithNextDestImpl(
                "mf2t1s", 100, 500,  eventProcessor, 2);
        
        DelayActorMockWithNextDestImpl queueItemF2T5T = new DelayActorMockWithNextDestImpl("mf2t5",
                100, 500,  eventProcessor,8);
        DelayActorMockWithNextDestImpl queueItemF2T5TS = new DelayActorMockWithNextDestImpl(
                "mf2t5s", 100, 500, eventProcessor,8);

        DelayActorMockWithNextDestImpl queueItemF5T2T = new DelayActorMockWithNextDestImpl("mf5t2",
                100, 500,  eventProcessor,1);
        DelayActorMockWithNextDestImpl queueItemF5T2TS = new DelayActorMockWithNextDestImpl(
                "mf5t2s", 100, 500, eventProcessor,1);

        DelayActorMockWithNextDestImpl queueItemF5T8T = new DelayActorMockWithNextDestImpl("mf5t8",
                100, 500,  eventProcessor,5);
        DelayActorMockWithNextDestImpl queueItemF5T8TS = new DelayActorMockWithNextDestImpl(
                "mf5t8s", 100, 500, eventProcessor,5);

        DelayActorMockWithNextDestImpl queueItemF8T5T = new DelayActorMockWithNextDestImpl("mf8t5",
                100, 500,  eventProcessor,2);
        DelayActorMockWithNextDestImpl queueItemF8T5TS = new DelayActorMockWithNextDestImpl(
                "mf8t5s", 100, 500, eventProcessor,2);

        
        
        DelayModelImpl queueResolverNew = new DelayModelImpl(queueMock.queues, eventProcessor,
                queueResolverStrategy);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.CAR, queueItemF1T2T);
        queueResolverNew.handleDelayActor(1, 2, EQueueMockGraphType.CAR, queueItemF1T2TS);
        queueResolverNew.handleDelayActor(2, 1, EQueueMockGraphType.CAR, queueItemF2T1T);
        queueResolverNew.handleDelayActor(2, 1, EQueueMockGraphType.CAR, queueItemF2T1TS);
        
        queueResolverNew.handleDelayActor(2,5, EQueueMockGraphType.CAR, queueItemF2T5T);
        queueResolverNew.handleDelayActor(2,5, EQueueMockGraphType.CAR, queueItemF2T5TS);
        queueResolverNew.handleDelayActor(5,2, EQueueMockGraphType.CAR, queueItemF5T2T);
        queueResolverNew.handleDelayActor(5,2, EQueueMockGraphType.CAR, queueItemF5T2TS);
        
        queueResolverNew.handleDelayActor(5, 8, EQueueMockGraphType.CAR, queueItemF5T8T);
        queueResolverNew.handleDelayActor(5, 8, EQueueMockGraphType.CAR, queueItemF5T8TS);
        queueResolverNew.handleDelayActor(8, 5, EQueueMockGraphType.CAR, queueItemF8T5T);
        queueResolverNew.handleDelayActor(8, 5, EQueueMockGraphType.CAR, queueItemF8T5TS);
        
        assertEquals(0, eventProcessor.getCurrentTime());

        eventProcessor.run();

        assertFalse(queueItemF1T2T.wasExecuted);
        assertFalse(queueItemF1T2TS.wasExecuted);
        assertFalse(queueItemF2T1T.wasExecuted);
        assertFalse(queueItemF2T1TS.wasExecuted);

        assertFalse(queueItemF2T5T.wasExecuted);
        assertFalse(queueItemF2T5TS.wasExecuted);
        assertFalse(queueItemF5T2T.wasExecuted);
        assertFalse(queueItemF5T2TS.wasExecuted);
        
        assertFalse(queueItemF5T8T.wasExecuted);
        assertFalse(queueItemF5T8TS.wasExecuted);
        assertFalse(queueItemF8T5T.wasExecuted);
        assertFalse(queueItemF8T5TS.wasExecuted);
     
    }
    
    private static class DelayActorMockImpl implements DelayActor {

        public boolean wasExecuted = false;
        public long executionTime = 0;

        private final String queueEntityId;
        private final long queueTimeDelay;
        private final double takenCapacitytakenCapacity;

        private final EventProcessor eventProcessor;

        public DelayActorMockImpl(String queueEntityId, long queueTimeDelay,
                double takenCapacitytakenCapacity, EventProcessor eventProcessor) {
            super();
            this.queueEntityId = queueEntityId;
            this.queueTimeDelay = queueTimeDelay;
            this.takenCapacitytakenCapacity = takenCapacitytakenCapacity;
            this.eventProcessor = eventProcessor;
        }

        @Override
        public void execute() {
            wasExecuted = true;
            this.executionTime = eventProcessor.getCurrentTime();
        }

        @Override
        public Long nextDestinationByNodeId() {
            return null;
        }

        @Override
        public String delayActorId() {
            return queueEntityId;
        }

        @Override
        public long delayTime() {
            return queueTimeDelay;
        }

        @Override
        public double takenSpace() {
            return takenCapacitytakenCapacity;
        }

    }

    private static class DelayActorMockWithNextDestImpl extends DelayActorMockImpl {

        private final long nextDest;

        public DelayActorMockWithNextDestImpl(String queueEntityId, long queueTimeDelay,
                double takenCapacitytakenCapacity, EventProcessor eventProcessor,long nextDest) {
            super(queueEntityId, queueTimeDelay, takenCapacitytakenCapacity, eventProcessor);            
                this.nextDest = nextDest;
        }

        @Override
        public Long nextDestinationByNodeId() {
            return nextDest;
        }

    }

}
