/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.agent;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 * @param <V>
 */
public interface Driver<V extends Vehicle> extends MovingAgent{
    public V getVehicle();
    
    public void startDriving(V vehicle);
    
    public void endDriving();
    
    public void setPosition(SimulationNode position);

}
