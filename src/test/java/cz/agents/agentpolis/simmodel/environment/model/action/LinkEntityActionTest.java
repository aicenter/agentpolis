package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.action.LinkEntityAction;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;

public class LinkEntityActionTest {

	Mocks mocks;

	@Before
	public void setUp() {
		mocks = new Mocks();
	}

	@Test
	public void linkEntityAction() {


		String linkedEntityById = "linkedEntityById";
		LinkedEntitySensor linkedEntityCallback = mock(LinkedEntitySensor.class);
		
		Set<String> linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.walkerAgent.getId());
		assertEquals(0, linkedEntities.size());
		
		LinkEntityAction linkEntityAction = mocks.linkEntityAction;		
		linkEntityAction.linkEnities(mocks.walkerAgent.getId(), linkedEntityById, linkedEntityCallback);
		
		linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.walkerAgent.getId());
		assertEquals(1, linkedEntities.size());
		
		assertEquals(linkedEntityById, linkedEntities.iterator().next());
		
		linkEntityAction.unLinkEnitiesWithoutCalling(linkedEntityById);
		linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.walkerAgent.getId());
		assertEquals(0, linkedEntities.size());
		
		
	}
}
