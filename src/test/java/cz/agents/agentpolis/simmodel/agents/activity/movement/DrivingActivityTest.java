package cz.agents.agentpolis.simmodel.agents.activity.movement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.siminfrastructure.planner.trip.DepartureTripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.NotWaitingDepartureTripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.PTVehilceTrip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.WaitingDepartureTripItem;
import cz.agents.agentpolis.simmodel.agent.activity.movement.DriveVehicleActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.DrivingFinishedActivityCallback;

public class DrivingActivityTest {

	Mocks mocks;

	DrivingFinishedActivityCallback drivingActivityCallback = mock(DrivingFinishedActivityCallback.class);

	@Before
	public void setUp() {
		mocks = new Mocks();
	}

	@Test
	public void drivingActivityTest() {

		LinkedList<TripItem> destinations = new LinkedList<>();
		destinations.add(new TripItem(GraphMock.node1.id));
		destinations.add(new TripItem(GraphMock.node2.id));
		destinations.add(new TripItem(GraphMock.node3.id));

		VehicleTrip trip = new VehicleTrip(destinations, GraphMock.EGraphTypeMock.GRAPH1, mocks.vehicle.getId());

		mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.id);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, trip, drivingActivityCallback);

		mocks.eventProcessor.run();

		long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());

		assertEquals(GraphMock.node3.id, positionOfDriver);
		assertEquals(GraphMock.node3.id, positionOfVehicle);

		assertEquals(144751, mocks.eventProcessor.getCurrentTime());
	}

	@Test
	public void drivingActivityTest2() {

		LinkedList<DepartureTripItem> destinations = new LinkedList<>();
		destinations.add(
				new WaitingDepartureTripItem(GraphMock.node1.id, Duration.ofMinutes(1).toMillis(), false));
		destinations.add(
				new WaitingDepartureTripItem(GraphMock.node2.id, Duration.ofMinutes(10).toMillis(),
											 false));
		destinations.add(new NotWaitingDepartureTripItem(GraphMock.node3.id));

		PTVehilceTrip trip = new PTVehilceTrip(destinations, GraphMock.EGraphTypeMock.GRAPH1);

		mocks.vehicleTimeModel.addVehicleDepartureDayFlag(mocks.vehicle.getId(), 0);
		mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.id);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.driveBaseOnDepartureTime(mocks.driver.getId(), mocks.vehicle, trip, drivingActivityCallback);

		mocks.eventProcessor.run();

		long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());

		assertEquals(GraphMock.node3.id, positionOfDriver);
		assertEquals(GraphMock.node3.id, positionOfVehicle);

		assertEquals(672249, mocks.eventProcessor.getCurrentTime());
	}

	@Test
	public void drivingActivityTest3() {

		DrivingActivityCallbackMock mock = new DrivingActivityCallbackMock();

		LinkedList<TripItem> destinations = new LinkedList<>();
		destinations.add(new TripItem(GraphMock.node1.id));
		destinations.add(new TripItem(GraphMock.node2.id));
		destinations.add(new TripItem(GraphMock.node3.id));

		VehicleTrip trip = new VehicleTrip(destinations, GraphMock.EGraphTypeMock.GRAPH1, mocks.vehicle.getId());

		mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.id);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, trip, drivingActivityCallback);

		mocks.eventProcessor.run();

		assertEquals(0, mock.numberOfLinkedEntities);
		assertEquals(0, mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId()).size());

		long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());

		assertEquals(GraphMock.node3.id, positionOfDriver);
		assertEquals(GraphMock.node3.id, positionOfVehicle);

		assertEquals(144751, mocks.eventProcessor.getCurrentTime());

	}

	private class DrivingActivityCallbackMock implements DrivingFinishedActivityCallback {

		private int numberOfLinkedEntities = 0;

		public void finishedDriving() {
			Set<String> linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId());
			numberOfLinkedEntities = linkedEntities.size();
		}

	}

}
