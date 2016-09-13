package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveEntityAction;
import cz.agents.alite.common.event.EventProcessor;

public class MoveToNextNodeActionTest {

	Mocks mocks;

	@Before
	public void setUp() {
		mocks = new Mocks();
	}

	@Test
	public void moveToNextNodeAction() {

		AgentPolisEntity relateyEntity = mock(AgentPolisEntity.class);
		when(relateyEntity.getId()).thenReturn("entityId");

		EventProcessor eventProcessor = mocks.eventProcessor;

		// when(positionSensor.getCurrentPositionBaseOnGraphType(relateyEntity.getId(),GraphMock.GraphType.GRAPH1))
		// .thenReturn(GraphMock.node1);
		// when(positionSensor.getCurrentPositionBaseOnGraphType(GraphMock.GraphType.GRAPH1,GraphMock.node2.getId()))
		// .thenReturn(GraphMock.node2);

		MoveEntityAction moveToNextNodeAction = mocks.moveToNextNodeAction;
		moveToNextNodeAction.moveToNode(relateyEntity.getId(), GraphMock.node2.id, 1, mocks.agentPositionModel);

		eventProcessor.run();

		assertEquals(GraphMock.node2.id, mocks.agentPositionModel.getEntityPositionByNodeId("entityId").longValue());

	}

}
