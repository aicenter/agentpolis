/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.MovingEntity;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.basestructures.Node;
import java.util.List;

/**
 *
 * @author fido
 */
public interface TransportAgent extends MovingEntity{
    
    public List<? extends AgentPolisEntity> getTransportedEntities();
    
    public void setTargetNode(Node targetNode);
    
    public void setDelayData(DelayData targetNode);
}
