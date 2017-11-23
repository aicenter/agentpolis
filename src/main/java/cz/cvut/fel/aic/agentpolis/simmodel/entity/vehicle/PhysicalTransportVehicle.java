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
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fido
 * @param <T>
 */
public class PhysicalTransportVehicle<T extends TransportableEntity> extends PhysicalVehicle implements TransportEntity<T>{
    
    protected final List<T> transportedEntities;
    
    public PhysicalTransportVehicle(String vehicleId, EntityType type, double lengthInMeters, int vehiclePassengerCapacity, 
            GraphType usingGraphTypeForMoving, SimulationNode position, double maxVelocity) {
        super(vehicleId, type, lengthInMeters, vehiclePassengerCapacity, usingGraphTypeForMoving, position, maxVelocity);
        transportedEntities = new LinkedList<>();
    }

    @Override
    public List<T> getTransportedEntities() {
        return transportedEntities; 
    }
    
    public void pickUp(T entity) {
        transportedEntities.add(entity);
        entity.setTransportingEntity(this);
    }
    
    public void dropOff(T entityToDropOff) {
        transportedEntities.remove(entityToDropOff);
        entityToDropOff.setTransportingEntity(null);
    }

    @Override
    public void setPosition(SimulationNode position) {
        super.setPosition(position); 
        for (T transportedEntity : transportedEntities) {
            transportedEntity.setPosition(position);
        }
    }
    
    
    
    
}
