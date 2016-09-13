package cz.agents.agentpolis.simmodel.environment.model.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.passenger.WaitForVehicleAction;

public class PassengerWaitingActionTest {

	Mocks mocks;
	
	@Before
	public void setUp(){
		mocks = new Mocks();
	}

	
	@Test
	public void passengerWaitingActionTest(){
		
		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);
		long waitingOnNodeById = 1;
		
		
		WaitForVehicleAction passengerWaitingAction = mocks.passengerWaitingAction;
		passengerWaitingAction.waitToVehicle(mocks.passenger.getId(),mocks.vehicle.getId(), waitingOnNodeById, vehiclePlanCallback);
		
		assertEquals(1, mocks.waitingPassengersOnSpecificPosition.size());
		assertEquals(1, mocks.passengerAndVehiclePlanCallback.size());		
		
			
		
	}
	
	@SuppressWarnings("static-access")
    @Test
	public void passengerWaitingActionTest2(){
		
		VehicleArrivedCallback vehiclePlanCallback = mock(VehicleArrivedCallback.class);
		long waitingOnNodeById = 1;
		
		WaitForVehicleAction passengerWaitingAction = mocks.passengerWaitingAction;
		passengerWaitingAction.waitToVehicleFromGroup(mocks.passenger.getId(),mocks.GROUP_ID, waitingOnNodeById, vehiclePlanCallback);				
		
		assertEquals(2, mocks.waitingPassengersOnSpecificPosition.size());
		assertEquals(1, mocks.passengerAndVehiclePlanCallback.size());		
		
		
//		assertEquals(2, mocks.callbackBoundedWithPositionVPS.size());		
//		assertNotNull(mocks.callbackBoundedWithPositionVPS.get(new KeyWithString(waitingOnNodeById, mocks.groupVehicle1.getId())));
//		assertNotNull(mocks.callbackBoundedWithPositionVPS.get(new KeyWithString(waitingOnNodeById, mocks.groupVehicle2.getId())));
//		
		
	}
}
