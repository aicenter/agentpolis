/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.CongestionTestType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public class DriveAgent extends Agent implements Driver<PhysicalVehicle>{
	
	private DelayData delayData;
	
	private PhysicalVehicle drivenCar;
	
	private SimulationNode targetNode;

	public DriveAgent(String agentId, SimulationNode position) {
		super(agentId, position);
	}

	@Override
	public EntityType getType() {
		return CongestionTestType.TEST_DRIVER;
	}

	@Override
	public PhysicalVehicle getVehicle() {
		return drivenCar;
	}

	@Override
	public void startDriving(PhysicalVehicle vehicle){
		this.drivenCar = vehicle;
		vehicle.setDriver(this);
	}
	
	@Override
	public void endDriving(){
		drivenCar.setDriver(null);
		this.drivenCar = null;
	}

	

	@Override
	public void setTargetNode(SimulationNode targetNode) {
		this.targetNode = targetNode;
	}

	@Override
	public SimulationNode getTargetNode() {
		return targetNode;
	}

	@Override
	public void setDelayData(DelayData delayData) {
		this.delayData = delayData;
	}

	@Override
	public DelayData getDelayData() {
		return delayData;
	}

        
        /**
         * @return vehicle speed in [m/s]
         */
	@Override
	public double getVelocity() {
		return 15;
	}
	
}
