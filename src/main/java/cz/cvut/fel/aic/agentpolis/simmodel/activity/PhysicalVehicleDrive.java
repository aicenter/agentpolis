
package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;

public abstract class PhysicalVehicleDrive<A extends Agent & Driver> extends Activity<A>{

	public PhysicalVehicleDrive(ActivityInitializer activityInitializer, A agent) {
		super(activityInitializer, agent);
	}
	
	public abstract void end();
}
