package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.key.VehicleAndPositionKey;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;

public class UseVehicleActionTest {

	Mocks mocks;

	@Before
	public void setUp() {
		mocks = new Mocks();
	}

	@Test
	public void useVehicleAction5() {

		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);

		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(), GraphMock.node1.id);

		PassengerAction useVehicleAction = mocks.useVehicleAction;

		useVehicleAction.waitToVehicle(mocks.passenger.getId(), mocks.vehicle.getId(), vehiclePlanCallback);

		assertEquals(1, mocks.waitingPassengersOnSpecificPosition.size());
		assertEquals(1, mocks.passengerAndVehiclePlanCallback.size());

		assertNotNull(mocks.waitingPassengersOnSpecificPosition.get(new VehicleAndPositionKey(GraphMock.node1.id,
				mocks.vehicle.getId())));

	}

	@SuppressWarnings("static-access")
	@Test
	public void useVehicleAction6() {

		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);

		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(), GraphMock.node1.id);

		PassengerAction useVehicleAction = mocks.useVehicleAction;
		useVehicleAction.waitToVehicleFromGroup(mocks.passenger.getId(), mocks.GROUP_ID, vehiclePlanCallback);

		assertEquals(2, mocks.waitingPassengersOnSpecificPosition.size());
		assertEquals(1, mocks.passengerAndVehiclePlanCallback.size());

		assertNotNull(mocks.waitingPassengersOnSpecificPosition.get(new VehicleAndPositionKey(GraphMock.node1.id,
				mocks.groupVehicle1.getId())));
		assertNotNull(mocks.waitingPassengersOnSpecificPosition.get(new VehicleAndPositionKey(GraphMock.node1.id,
				mocks.groupVehicle2.getId())));

	}

	@Test
	public void useVehicleAction2() {

		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(), GraphMock.node1.id);

		LinkedEntitySensor linkedEntityCallback = mock(LinkedEntitySensor.class);

		PassengerAction useVehicleAction = mocks.useVehicleAction;
		useVehicleAction.getInVehicle(mocks.passenger.getId(), mocks.vehicle.getId(), vehiclePlanCallback,
				linkedEntityCallback);

		Set<String> linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId());

		assertEquals(1, linkedEntities.size());
		assertEquals(mocks.passenger.getId(), linkedEntities.iterator().next());

	}

	@Test
	public void useVehicleAction3() {

		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(), GraphMock.node1.id);

		LinkedEntitySensor linkedEntityCallback = mock(LinkedEntitySensor.class);

		PassengerAction useVehicleAction = mocks.useVehicleAction;
		useVehicleAction.getInVehicle(mocks.passenger.getId(), mocks.vehicle.getId(), vehiclePlanCallback,
				linkedEntityCallback);

		Set<String> linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId());
		assertEquals(1, linkedEntities.size());
		assertEquals(mocks.passenger.getId(), linkedEntities.iterator().next());

		useVehicleAction.getOffVehicleAndUnLink(mocks.passenger.getId(), mocks.vehicle.getId());

		linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId());
		assertEquals(0, linkedEntities.size());

	}

}
