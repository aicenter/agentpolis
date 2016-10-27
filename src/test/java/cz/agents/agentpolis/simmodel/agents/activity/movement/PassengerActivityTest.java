package cz.agents.agentpolis.simmodel.agents.activity.movement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.agent.activity.movement.DriveVehicleActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.RideAsPassengerActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.RideInVehicleActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.DrivingFinishedActivityCallback;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;

@SuppressWarnings("static-access")
public class PassengerActivityTest {

	LinkedList<TripItem> driverDesitnation;
	VehicleTrip tripDriver;
	PassengerCallbackImpl passengerCallbackImpl;
	
	Mocks mocks;
	
	DrivingFinishedActivityCallback drivingActivityCallback = mock(DrivingFinishedActivityCallback.class);
	
	
	@Before
	public void setUp(){
		
		mocks = new Mocks();
		
		driverDesitnation = new LinkedList<>();
		driverDesitnation.add(new TripItem(GraphMock.node1.id));
		driverDesitnation.add(new TripItem(GraphMock.node2.id));
		driverDesitnation.add(new TripItem(GraphMock.node3.id));
		driverDesitnation.add(new TripItem(GraphMock.node4.id));
		driverDesitnation.add(new TripItem(GraphMock.node5.id));


		tripDriver = new VehicleTrip(driverDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());

		passengerCallbackImpl = new PassengerCallbackImpl();

	}



	@Test
	public void passengerActivity(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
//		passengerDesitnation.add(GraphMock.node1.id);
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));


		mocks.vehiclePositionModel.setNewEntityPosition(mocks.vehicle.getId(),GraphMock.node1.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node3.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleAsPassenger(mocks.passenger.getId(), mocks.vehicle.getId(), passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, tripDriver,drivingActivityCallback);


		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node4.id, passengerPosition);

		assertTrue(passengerCallbackImpl.tripDone);
	}

	@Test
	public void passengerActivity2(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node1.id));
		passengerDesitnation.add(new TripItem(GraphMock.node2.id));
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));

		mocks.vehiclePositionModel.setNewEntityPosition(mocks.vehicle.getId(),GraphMock.node1.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node1.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleAsPassenger(mocks.passenger.getId(), mocks.vehicle.getId(), passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, tripDriver,drivingActivityCallback);


		mocks.eventProcessor.run();


		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node3.id, passengerPosition);

		assertTrue(passengerCallbackImpl.tripDone);
	}


	@Test
	public void passengerActivity3(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node2.id));
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));

		mocks.vehiclePositionModel.setNewEntityPosition(mocks.vehicle.getId(),GraphMock.node1.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node2.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


        RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleAsPassenger(mocks.passenger.getId(), mocks.vehicle.getId(), passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, tripDriver,drivingActivityCallback);

		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node4.id, passengerPosition);

		assertTrue(passengerCallbackImpl.tripDone);
	}


	@Test
	public void passengerActivity4(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node2.id));
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));
		passengerDesitnation.add(new TripItem(GraphMock.node5.id));


		LinkedList<TripItem> driverDesitnation = new LinkedList<>();
		driverDesitnation.add(new TripItem(GraphMock.node1.id));
		driverDesitnation.add(new TripItem(GraphMock.node2.id));
		driverDesitnation.add(new TripItem(GraphMock.node3.id));
		driverDesitnation.add(new TripItem(GraphMock.node4.id));


		VehicleTrip tripDriverNew = new VehicleTrip(driverDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


		mocks.vehiclePositionModel.setNewEntityPosition(mocks.vehicle.getId(),GraphMock.node1.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node2.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleAsPassenger(mocks.passenger.getId(), mocks.vehicle.getId(), passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, tripDriverNew,drivingActivityCallback);

		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node4.id, driverPosition);
		assertEquals(GraphMock.node4.id, vehiclePosition);
		assertEquals(GraphMock.node4.id, passengerPosition);


		assertTrue(passengerCallbackImpl.tripPartDone);


	}

	@Test
	public void passengerActivity5(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node2.id));
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));
		passengerDesitnation.add(new TripItem(GraphMock.node5.id));

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());

		LinkedList<TripItem> passenger2Desitnation = new LinkedList<>();
		passenger2Desitnation.add(new TripItem(GraphMock.node3.id));
		passenger2Desitnation.add(new TripItem(GraphMock.node4.id));
		passenger2Desitnation.add(new TripItem(GraphMock.node5.id));

		VehicleTrip passenger2Driver = new VehicleTrip(passenger2Desitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());

		mocks.vehiclePositionModel.setNewEntityPosition(mocks.vehicle.getId(),GraphMock.node1.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node2.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger2.getId(),GraphMock.node3.id);


		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.injector.getInstance(RideInVehicleActivity.class);
		passengerActivity.usingVehicleAsPassenger(mocks.passenger.getId(), mocks.vehicle.getId(), passengerDriver,passengerCallbackImpl);

		PassengerCallbackImpl passengerCallbackImpl2 = new PassengerCallbackImpl();

		RideAsPassengerActivity<VehicleTrip> passenger2Activity = mocks.injector.getInstance(RideInVehicleActivity.class);
		passenger2Activity.usingVehicleAsPassenger(mocks.passenger2.getId(), mocks.vehicle.getId(),passenger2Driver,passengerCallbackImpl2);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.vehicle, tripDriver,drivingActivityCallback);


		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());
		long passenger2Position = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger2.getId());


		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node5.id, passengerPosition);
		assertEquals(GraphMock.node3.id, passenger2Position);

		assertTrue(passengerCallbackImpl2.tripFailed);




	}

	@Test
	public void passengerActivity6(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));


		mocks.vehiclePositionModel.setNewEntityPosition(mocks.groupVehicle1.getId(),GraphMock.node1.id);
		mocks.vehiclePositionModel.setNewEntityPosition(mocks.groupVehicle2.getId(),GraphMock.node2.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node3.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());



		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleFromGroupAsPassenger(mocks.passenger.getId(), Mocks.GROUP_ID, passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.vehicleDrivingActivity;
		drivingActivity.drive(mocks.driver.getId(), mocks.groupVehicle1, tripDriver,drivingActivityCallback);


		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.groupVehicle1.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node4.id, passengerPosition);

		assertTrue(passengerCallbackImpl.tripDone);
	}


    @Test
	public void passengerActivity7(){

		LinkedList<TripItem> passengerDesitnation = new LinkedList<>();
		passengerDesitnation.add(new TripItem(GraphMock.node3.id));
		passengerDesitnation.add(new TripItem(GraphMock.node4.id));

		LinkedList<TripItem> driverDesitnation2 = new LinkedList<>();
		driverDesitnation2.add(new TripItem(GraphMock.node3.id));
		driverDesitnation2.add(new TripItem(GraphMock.node4.id));

		VehicleTrip tripDriver2 = new VehicleTrip(driverDesitnation2, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());


		mocks.vehiclePositionModel.setNewEntityPosition(mocks.groupVehicle1.getId(),GraphMock.node1.id);
		mocks.vehiclePositionModel.setNewEntityPosition(mocks.groupVehicle2.getId(),GraphMock.node3.id);
		mocks.agentPositionModel.setNewEntityPosition(mocks.passenger.getId(),GraphMock.node3.id);

		VehicleTrip passengerDriver = new VehicleTrip(passengerDesitnation, GraphMock.EGraphTypeMock.GRAPH1,mocks.vehicle.getId());

		RideAsPassengerActivity<VehicleTrip> passengerActivity = mocks.passengerActivityVehicleTrip;
		passengerActivity.usingVehicleFromGroupAsPassenger(mocks.passenger.getId(), mocks.GROUP_ID, passengerDriver,passengerCallbackImpl);

		DriveVehicleActivity drivingActivity = mocks.injector.getInstance(DriveVehicleActivity.class);
		drivingActivity.drive(mocks.driver.getId(), mocks.groupVehicle1, tripDriver,drivingActivityCallback);


		DriveVehicleActivity drivingActivity2 = mocks.injector.getInstance(DriveVehicleActivity.class);
		drivingActivity2.drive(mocks.driver2.getId(), mocks.groupVehicle2, tripDriver2,drivingActivityCallback);



		mocks.eventProcessor.run();

		long driverPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver.getId());
		long vehiclePosition = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.groupVehicle1.getId());
		long driver2Position = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver2.getId());
		long vehicle2Position = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.groupVehicle2.getId());
		long passengerPosition = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.passenger.getId());

		assertEquals(GraphMock.node5.id, driverPosition);
		assertEquals(GraphMock.node5.id, vehiclePosition);
		assertEquals(GraphMock.node4.id, driver2Position);
		assertEquals(GraphMock.node4.id, vehicle2Position);
		assertEquals(GraphMock.node4.id, passengerPosition);
		
		
		assertTrue(passengerCallbackImpl.tripDone);
	}
	
    @SuppressWarnings("unused")
	private class PassengerCallbackImpl implements PassengerActivityCallback<VehicleTrip> {

		public boolean tripDone = false;
		public boolean tripPartDone = false;
		public boolean tripFailed = false;
		
        public VehicleTrip partNotDoneTrip = null;
		public VehicleTrip failedTrip = null;
		
		@Override
		public void doneFullTrip() {
			tripDone = true;
		}

		@Override
		public void donePartTrip(VehicleTrip partNotDoneTrip) {
			tripPartDone = true;
			this.partNotDoneTrip = partNotDoneTrip;
		}

		@Override
		public void tripFail(VehicleTrip failedTrip) {
			tripFailed = true;
			this.failedTrip = failedTrip;
			
		}

        @Override
        public void tripStarted() {
            
        }
		
	}
	
	
}
