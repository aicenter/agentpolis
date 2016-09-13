package cz.agents.agentpolis.simmodel.agent.activity.movement;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.MovementActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingAction;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class MovementActivityTripItemTest {
    Mocks mocks;
    MovementActivityTripItem movementActivityDepartureTripItem;
    MovementActivityCallback movementCallback;
    MovingAction<TripItem> movingAction;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mocks = new Mocks();

        movementActivityDepartureTripItem = mocks.injector
                .getInstance(MovementActivityTripItem.class);

        movementCallback = mock(MovementActivityCallback.class);
        movingAction = mock(MovingAction.class);
    }

    @Test
    public void test() {

        LinkedList<TripItem> destinations = new LinkedList<>();
        destinations.add(new TripItem(GraphMock.node1.id));
        destinations.add(new TripItem(GraphMock.node2.id));
        destinations.add(new TripItem(GraphMock.node3.id));

        VehicleTrip trip = new VehicleTrip(destinations, GraphMock.EGraphTypeMock.GRAPH1,
                mocks.vehicle.getId());

        doAnswer(invocation -> {
			assertTrue(true);
			return null;
		}).when(movingAction).waitToDepartureTime(destinations.peek(), destinations.get(1),
                movementActivityDepartureTripItem);

        movementActivityDepartureTripItem.move("agentId", movementCallback, movingAction, trip);

    }

    @Test
    public void test1() {

        LinkedList<TripItem> destinations = new LinkedList<>();
        destinations.add(new TripItem(GraphMock.node1.id));
        destinations.add(new TripItem(GraphMock.node2.id));
        destinations.add(new TripItem(GraphMock.node3.id));

        VehicleTrip trip = new VehicleTrip(destinations, null, mocks.vehicle.getId());

        doAnswer(invocation -> {
			assertFalse("Activity was not stopped", true);
			return null;
		}).when(movingAction).waitToDepartureTime(destinations.peek(), destinations.get(1),
                movementActivityDepartureTripItem);

        movementActivityDepartureTripItem.move("agentId", movementCallback, movingAction, trip);
    }

    @Test
    public void test2() {

        LinkedList<TripItem> destinations = new LinkedList<>();
        destinations.add(new TripItem(GraphMock.node1.id));
        destinations.add(new TripItem(GraphMock.node2.id));
        destinations.add(new TripItem(-1));

        VehicleTrip trip = new VehicleTrip(destinations, null, mocks.vehicle.getId());

        doAnswer(invocation -> {
			assertFalse("Activity was not stopped", true);
			return null;
		}).when(movingAction).waitToDepartureTime(destinations.peek(), destinations.get(1),
                movementActivityDepartureTripItem);

        movementActivityDepartureTripItem.move("agentId", movementCallback, movingAction, trip);
    }
}