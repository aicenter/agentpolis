/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.MovingEntity;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import java.util.List;

/**
 *
 * @author fido
 */
public interface TransportEntity extends MovingEntity{
    
    public List<TransportableEntity> getTransportedEntities();
    
    
}
