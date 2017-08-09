/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.entity;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public interface TransportableEntity {
    public <T extends TransportEntity> T getTransportingEntity();
    
    public <T extends TransportEntity> void setTransportingEntity(T transportingEntity);
    
    public void setPosition(SimulationNode position);
}
