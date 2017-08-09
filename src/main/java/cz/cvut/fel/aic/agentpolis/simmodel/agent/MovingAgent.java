/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;

/**
 * @author fido
 */
public interface MovingAgent extends MovingEntity {
    public void setTargetNode(SimulationNode targetNode);

    public void setDelayData(DelayData targetNode);

    public SimulationNode getTargetNode();

    public DelayData getDelayData();

    public SimulationNode getPosition();
}
