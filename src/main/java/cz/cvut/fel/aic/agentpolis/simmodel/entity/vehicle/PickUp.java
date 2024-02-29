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
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public final class PickUp {

	public static <T extends TransportableEntity, E extends TransportEntity<T>> void pickUp(
		T entity,
		E transportingEntity
	) {
		if (!transportingEntity.hasCapacityFor(entity)) {
			try {
				throw new Exception(
						String.format("Cannot pick up entity, the vehicle is full! [%s]", entity));
			} catch (Exception ex) {
				Logger.getLogger(PhysicalTransportVehicle.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if(entity.getTransportingEntity() != null){
			try {
				throw new Exception(
						String.format("Cannot pick up entity, it's already being transported! [%s]", entity));
			} catch (Exception ex) {
				Logger.getLogger(PhysicalTransportVehicle.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else{
			transportingEntity.getTransportedEntities().add(entity);
			entity.setTransportingEntity(transportingEntity);
			transportingEntity.runPostPickUpActions(entity);
		}
	}

}
