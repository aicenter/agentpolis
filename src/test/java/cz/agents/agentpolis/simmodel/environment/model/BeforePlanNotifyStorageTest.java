package cz.agents.agentpolis.simmodel.environment.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PassengerBeforePlanNotifySensor;

public class BeforePlanNotifyStorageTest {

	Map<String, PassengerBeforePlanNotifySensor> passengerBeforePlanNotifySensorCallbackBPNS = new HashMap<String, PassengerBeforePlanNotifySensor>();
	Map<String, Set<String>> currentlyInformPassengersBeforePlanNotifyBPNS = new HashMap<String, Set<String>>();
	Map<String, String> passengerIdLinkedWithVehicleIdBPNS = new HashMap<String, String>();
	Map<String, MovingActionCallback> movingActionCallbackBPNS = new HashMap<String, MovingActionCallback>();

	BeforePlanNotifyModel beforePlanNotifyStorage;
	
	@Before
	public void setUp() {
		passengerBeforePlanNotifySensorCallbackBPNS = new HashMap<String, PassengerBeforePlanNotifySensor>();
		currentlyInformPassengersBeforePlanNotifyBPNS = new HashMap<String, Set<String>>();
		passengerIdLinkedWithVehicleIdBPNS = new HashMap<String, String>();		
		movingActionCallbackBPNS = new HashMap<String, MovingActionCallback>();
		
		beforePlanNotifyStorage = new BeforePlanNotifyModel(passengerBeforePlanNotifySensorCallbackBPNS, 
				currentlyInformPassengersBeforePlanNotifyBPNS, passengerIdLinkedWithVehicleIdBPNS, 
				movingActionCallbackBPNS,null);
	}

	@Test
	public void addPassengerForInform() {
		
		PassengerBeforePlanNotifySensor beforePlanNotifySensorCallback = mock(PassengerBeforePlanNotifySensor.class);
	
		assertTrue(passengerBeforePlanNotifySensorCallbackBPNS.isEmpty());
		beforePlanNotifyStorage.addPassengerForInform("passengerId", beforePlanNotifySensorCallback);
		assertFalse(passengerBeforePlanNotifySensorCallbackBPNS.isEmpty());
	
	}

	@Test
	public void removePassengerForInform() {
		
		PassengerBeforePlanNotifySensor beforePlanNotifySensorCallback = mock(PassengerBeforePlanNotifySensor.class);
		beforePlanNotifyStorage.addPassengerForInform("passengerId", beforePlanNotifySensorCallback);
		assertFalse(passengerBeforePlanNotifySensorCallbackBPNS.isEmpty());
		beforePlanNotifyStorage.removePassengerForInform("passengerId");
		assertTrue(passengerBeforePlanNotifySensorCallbackBPNS.isEmpty());
		
		
	}

	@Test
	public void callBeforePlanNotify() {
		// String vehicleId, Set<String>
		// passengersIncludedDriverInVehilce,VehicleBeforePlanNotifySensorCallback
		// vehicleBeforePlanNotifySensorCallback,MovingActionCallback
		// movingActionCallback
		
		
		
		
	}

	@Test
	public void afterLastPassengerForInformInvokeCallbackForVehicle() {
		// String passengerId
	}

}
