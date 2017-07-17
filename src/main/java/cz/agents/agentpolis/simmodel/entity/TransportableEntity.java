/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity;

import cz.agents.agentpolis.simmodel.agent.TransportEntity;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
public interface TransportableEntity {
    public <T extends TransportEntity> T getTransportingEntity();
    
    public <T extends TransportEntity> void setTransportingEntity(T transportingEntity);
    
    public void setPosition(SimulationNode position);
}
