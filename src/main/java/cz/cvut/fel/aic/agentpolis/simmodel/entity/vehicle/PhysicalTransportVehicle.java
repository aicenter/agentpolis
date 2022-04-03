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

import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fido
 * @param <T>
 */
public class PhysicalTransportVehicle extends PhysicalVehicle implements TransportEntity {

	protected final List<TransportableEntity> transportedEntities;

	private final int vehiclePassengerCapacity; // number of passenger, including driver
	
		
	private boolean highlited;

	
	
	
	public int getCapacity() {
		return vehiclePassengerCapacity;
	}
	
	public boolean isHighlited() {
		return highlited;
	}

	public void setHighlited(boolean highlited) {
		this.highlited = highlited;
	}

	
	
	
	public PhysicalTransportVehicle(String vehicleId, EntityType type, float lengthInMeters, int vehiclePassengerCapacity,
			GraphType usingGraphTypeForMoving, SimulationNode position, int maxVelocity) {
		super(vehicleId, type, lengthInMeters, usingGraphTypeForMoving, position, maxVelocity);
		this.vehiclePassengerCapacity = vehiclePassengerCapacity;
		transportedEntities = new LinkedList<>();
	}

	@Override
	public List<TransportableEntity> getTransportedEntities() {
		return transportedEntities;
	}

	public void pickUp(TransportableEntity entity) {
		PickUp.pickUp(entity, transportedEntities.size() == vehiclePassengerCapacity, this, transportedEntities);
	}

	public void dropOff(TransportableEntity entityToDropOff) {
		boolean success = transportedEntities.remove(entityToDropOff);
		if (!success) {
			try {
				throw new Exception(
						String.format("Cannot drop off entity, it is not transported! [%s]", entityToDropOff));
			} catch (Exception ex) {
				Logger.getLogger(PhysicalTransportVehicle.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		entityToDropOff.setTransportingEntity(null);
	}

	@Override
	public void setPosition(SimulationNode position) {
		super.setPosition(position);
		for (TransportableEntity transportedEntity : transportedEntities) {
			transportedEntity.setPosition(position);
		}
	}

}
