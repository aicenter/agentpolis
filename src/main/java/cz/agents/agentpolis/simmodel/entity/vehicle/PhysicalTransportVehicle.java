/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.simmodel.agent.TransportEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.Node;
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
            GraphType usingGraphTypeForMoving, Node position, double maxVelocity) {
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
    public void setPosition(Node position) {
        super.setPosition(position); 
        for (T transportedEntity : transportedEntities) {
            transportedEntity.setPosition(position);
        }
    }
    
    
    
    
}
