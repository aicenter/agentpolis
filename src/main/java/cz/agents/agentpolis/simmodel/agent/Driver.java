/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;

/**
 *
 * @author fido
 */
public interface Driver extends MovingAgent{
    public Vehicle getVehicle();
    
    public void startDriving(Vehicle vehicle);
    
    public void endDriving();
}
