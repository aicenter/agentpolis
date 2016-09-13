package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.simmodel.environment.model.action.walking.WalkingAction;


public class WalkingActionTest {

	Mocks mocks;
	
	@Before
	public void setUp(){
		mocks = new Mocks();
	}
	
	@Test
	public void walkingAction(){
		
		WalkingAction action = mocks.walkingAction;
		action.walk(mocks.walkerAgent.getId(),GraphMock.node1.id, GraphMock.node2.id, GraphMock.EGraphTypeMock.GRAPH1);
		
		mocks.eventProcessor.run();
		
		long position = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.walkerAgent.getId());
		
		assertTrue(position == GraphMock.node2.id);
		
	}
	
}
