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
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @param <T>
 * @author fido
 */
public abstract class TransportVehicle<T extends TransportableEntity> extends Vehicle implements TransportEntity<T> {

    protected final List<T> transportedEntities;

    public void pickUp(T person) {
        PickUp.pickUp(person, transportedEntities.size() == this.getCapacity(), this, transportedEntities);
    }

    public void pickUp(List<T> entitiesToPickup) {
        for (T person : entitiesToPickup) {
            PickUp.pickUp(person, transportedEntities.size() == this.getCapacity(), this, transportedEntities);
        }
    }

    public void dropOff(T entityToDropOff) {
        transportedEntities.remove(entityToDropOff);
        setDropOffForTransportable(entityToDropOff);
    }

    public TransportVehicle(String id, SimulationNode position) {
        super(id, position);
        transportedEntities = new LinkedList<>();
    }

    @Override
    public List<T> getTransportedEntities() {
        return transportedEntities;
    }

//    @Override
//    public abstract int getCapacity();
    public void dropOffAll() {
        Iterator<T> transportedEntitiesIterator = transportedEntities.iterator();
        while (transportedEntitiesIterator.hasNext()) {
            T transportedEntity = transportedEntitiesIterator.next();
            transportedEntitiesIterator.remove();
            setDropOffForTransportable(transportedEntity);
        }
    }

    @Override
    public String toString() {
        return "TransportVehicle{"
                + "id=" + super.getId() + ", entitiesOnBoard=" + transportedEntities.size() + "}";
    }

    private void setDropOffForTransportable(T entityToDropOff) {
        entityToDropOff.setTransportingEntity(null);
        entityToDropOff.setLastFromPosition(getLastFromPosition());
    }
}
