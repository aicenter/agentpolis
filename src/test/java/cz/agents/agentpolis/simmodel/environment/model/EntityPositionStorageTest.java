package cz.agents.agentpolis.simmodel.environment.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;

public class EntityPositionStorageTest {

    private Mocks mocks;
    
    @Before
    public void setUp(){
        mocks = new Mocks();
    }
    
    @Test
    public void entityPositionStorageTest(){
        
        
        String entityId = "entityId";
        int nodeId = 1;
        
        
        PositionEntityCallbackMock positionEntityCallback = new PositionEntityCallbackMock();

        
        mocks.agentPositionModel.addPositionCallbackForNode(entityId, nodeId,  positionEntityCallback);
        mocks.agentPositionModel.setTargetPositionAndReturnIfWasSame(entityId, nodeId);
        mocks.agentPositionModel.setNewEntityPosition(entityId, nodeId);
        
        mocks.eventProcessor.run();
        
        assertTrue(positionEntityCallback.wasCalled);
    }
    
    @Test
    public void entityPositionStorageTest2(){
        
        String entityId = "entityId";
        int nodeId = 1;
        
        
        PositionEntityCallbackMock positionEntityCallback = new PositionEntityCallbackMock();
        
        mocks.agentPositionModel.addPositionCallbackForNode(entityId, nodeId,  positionEntityCallback);
        mocks.agentPositionModel.removePositionCallbackForNode(entityId, nodeId, positionEntityCallback);
        mocks.agentPositionModel.setTargetPositionAndReturnIfWasSame(entityId, nodeId);
        mocks.agentPositionModel.setNewEntityPosition(entityId, nodeId);
        
        mocks.eventProcessor.run();
        
        assertFalse(positionEntityCallback.wasCalled);
    }
    
    
    
    private class PositionEntityCallbackMock implements PositionUpdated{

        public boolean wasCalled = false;
        
        @Override
        public void newEntityPosition(String entityId, long nodeId) {
            wasCalled = true;
            
        }
        
    }
    
    
}
