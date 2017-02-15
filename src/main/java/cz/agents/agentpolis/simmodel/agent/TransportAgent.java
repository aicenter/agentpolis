/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.basestructures.Node;
import java.util.List;

/**
 *
 * @author fido
 */
public interface TransportAgent {
    
    public double getVelocity();
    
    public List<AgentPolisEntity> getCargo();
    
    public void setTargetNode(Node targetNode);
    
    public Node getTargetNode();
    
    public void setDelayData(DelayData targetNode);
    
    public DelayData getDelayData();
}
