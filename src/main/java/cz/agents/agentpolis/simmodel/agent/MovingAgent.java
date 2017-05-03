/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.MovingEntity;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
public interface MovingAgent extends MovingEntity{
    public void setTargetNode(Node targetNode);
    
    public void setDelayData(DelayData targetNode);
    
    public Node getTargetNode();
    
    public DelayData getDelayData();
    
    public double getVelocity();
    
    public Node getPosition();
}
