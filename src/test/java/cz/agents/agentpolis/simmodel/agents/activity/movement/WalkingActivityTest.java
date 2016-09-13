package cz.agents.agentpolis.simmodel.agents.activity.movement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.WalkTrip;
import cz.agents.agentpolis.simmodel.agent.activity.movement.WalkActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.WalkingActivityCallback;

public class WalkingActivityTest {

	Mocks mocks;

	WalkingActivityCallback walkingActivityCallback = mock(WalkingActivityCallback.class);

	@Before
	public void setUp() {
		mocks = new Mocks();
	}

	@Test
	public void walkingActivityTest() {

		LinkedList<TripItem> destinations = new LinkedList<>();
		destinations.add(new TripItem(GraphMock.node1.id));
		destinations.add(new TripItem(GraphMock.node2.id));
		destinations.add(new TripItem(GraphMock.node3.id));

		WalkTrip trip = new WalkTrip(destinations, GraphMock.EGraphTypeMock.GRAPH1);

		WalkActivity walkingActivity = mocks.walkingActivity;
		walkingActivity.walk(mocks.walkerAgent.getId(), trip, walkingActivityCallback);

		mocks.eventProcessor.run();

		long positionOfWalker = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.walkerAgent.getId());

		assertEquals(GraphMock.node3.id, positionOfWalker);

	}

}
