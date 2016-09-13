package cz.agents.agentpolis.simmodel.environment.model.sensor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cz.agents.agentpolis.mock.Mocks;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.query.VehicleInfoQuery;

public class VehicleSensorTest {

	Mocks mocks;
	
	@Before
	public void setUp(){
		mocks = new Mocks();
	}
	
	@Test
	public void vehicleSensorTest(){
		VehicleInfoQuery vehicleInfoSensor = new VehicleInfoQuery(mocks.entityVelocityModel);
		double currentVehicleVelocity = vehicleInfoSensor.getCurrrentVehicleVelocity(mocks.groupVehicle1.getId());
		
		assertEquals(47.3, currentVehicleVelocity,2);
		
	}
}
