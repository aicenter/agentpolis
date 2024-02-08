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
 * Transport vehicle with dimensions.
 * @author fido
 * @param <T>
 */
public abstract class PhysicalTransportVehicle<T extends TransportableEntity> extends PhysicalVehicle implements
		TransportEntity<T> {

	protected final List<T> transportedEntities;


	private boolean highlighted;



	@Override
	public List<T> getTransportedEntities() {
		return transportedEntities;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}


	public PhysicalTransportVehicle(
		String vehicleId,
		EntityType type,
		float lengthInMeters,
		GraphType usingGraphTypeForMoving,
		SimulationNode position,
		int maxVelocity
	) {
		super(vehicleId, type, lengthInMeters, usingGraphTypeForMoving, position, maxVelocity);
		transportedEntities = new LinkedList<>();
	}



	public abstract boolean canTransport(T entity);



	public void pickUp(T entity) {
		PickUp.pickUp(entity, this);
	}

	public void dropOff(T entityToDropOff) {
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

		runPostDropOffActions(entityToDropOff);
	}

	@Override
    public void setPosition(SimulationNode position) {
		super.setPosition(position);
		for (T transportedEntity : transportedEntities) {
			transportedEntity.setPosition(position);
		}
	}

}
