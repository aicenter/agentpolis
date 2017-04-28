/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity;

import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
public interface MovingEntity {
    public Node getTargetNode();
    
    public DelayData getDelayData();
    
    public double getVelocity();
}
