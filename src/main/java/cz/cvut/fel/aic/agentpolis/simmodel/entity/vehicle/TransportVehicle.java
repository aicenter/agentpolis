/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        transportedEntities.add(person);
        person.setTransportingEntity(this);
    }

    public void pickUp(List<T> entitiesToPickup) {
        for (T person : entitiesToPickup) {
            transportedEntities.add(person);
            person.setTransportingEntity(this);
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
        return "TransportVehicle{" +
                "id=" + super.getId() + ", entitiesOnBoard=" + transportedEntities.size() + "}";
    }

    private void setDropOffForTransportable(T entityToDropOff) {
        entityToDropOff.setTransportingEntity(null);
        entityToDropOff.setLastFromPosition(getLastFromPosition());
    }
}
