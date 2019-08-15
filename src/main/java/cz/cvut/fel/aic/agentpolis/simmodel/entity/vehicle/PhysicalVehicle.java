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
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * Representation of vehicle
 *
 * @author Zbynek Moler
 */
public class PhysicalVehicle extends Vehicle {

	private final int lengthInCm;
	private final EntityType vehicleType;
	private final GraphType usingGraphTypeForMoving;

	private final int maxVelocity;

	public PhysicalVehicle(String vehicleId, EntityType type, float lengthInMeters,
						   GraphType usingGraphTypeForMoving, SimulationNode position, int maxVelocity) {
		super(vehicleId, position);
		this.lengthInCm = Math.round(lengthInMeters * 100);
		this.vehicleType = type;
		this.usingGraphTypeForMoving = usingGraphTypeForMoving;
		this.maxVelocity = maxVelocity;
	}

	@Override
	public EntityType getType() {
		return vehicleType;
	}


	public int getLengthCm() {
		return lengthInCm;
	}
	
	public double getLengthM() {
		return lengthInCm / 1E2;
	}

	public GraphType getGraphForMovingBaseOnType() {
		return usingGraphTypeForMoving;
	}

	@Override
	public int getVelocity() {
		return maxVelocity;
	}

}
