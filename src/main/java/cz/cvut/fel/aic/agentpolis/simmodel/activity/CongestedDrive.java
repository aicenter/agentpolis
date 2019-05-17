/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;

/**
 *
 * @author fido
 * @param <A>
 */
public class CongestedDrive<A extends Agent & Driver<PhysicalVehicle>> extends PhysicalVehicleDrive<A> {
	
	private final CongestionModel congestionModel;
	
	private final Trip<SimulationNode> trip;
	
	private final PhysicalVehicle vehicle;

	public CongestedDrive(ActivityInitializer activityInitializer, A agent, CongestionModel congestionModel, 
			Trip<SimulationNode> trip, PhysicalVehicle vehicle) {
		super(activityInitializer, agent);
		this.congestionModel = congestionModel;
		this.trip = trip;
		this.vehicle = vehicle;
	}

	@Override
	protected void performAction() {
		agent.startDriving(vehicle);
		congestionModel.drive(vehicle, trip);
	}

	@Override
	public void processMessage(Message message) {
		if(message.getType() == CongestionMessage.DRIVING_FINISHED){
			agent.endDriving();
			finish();
		}
	}
}
