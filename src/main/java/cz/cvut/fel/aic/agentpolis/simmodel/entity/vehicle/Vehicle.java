/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * @author fido
 */
public abstract class Vehicle extends AgentPolisEntity implements MovingEntity {

	private Driver driver;

	private int queueBeforeVehicleLength;

	private SimulationNode lastFromPosition;

	public SimulationNode getLastFromPosition() {
		return lastFromPosition;
	}

	@Override
	public SimulationNode getTargetNode() {
		return ((driver != null) ? driver.getTargetNode() : null);
	}

	@Override
	public DelayData getDelayData() {
		return ((driver != null) ? driver.getDelayData() : null);
	}

	public void setLastFromPosition(SimulationNode lastTargetPosition) {
		this.lastFromPosition = lastTargetPosition;
	}


	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public int getQueueBeforeVehicleLength() {
		return queueBeforeVehicleLength;
	}

	public void setQueueBeforeVehicleLength(int queueBeforeVehicleLength) {
		this.queueBeforeVehicleLength = queueBeforeVehicleLength;
	}


	public Vehicle(String id, SimulationNode position) {
		super(id, position);
	}

	@Override
	public void setPosition(SimulationNode position) {
		super.setPosition(position);
		if (driver != null) {
			driver.setPosition(position);
		}
	}


}
