/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.MovingEntity;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
public abstract class Vehicle extends AgentPolisEntity implements MovingEntity{
    
    private Driver driver;
    
    private double queueBeforeVehicleLength; 
    

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getQueueBeforeVehicleLength() {
        return queueBeforeVehicleLength;
    }

    public void setQueueBeforeVehicleLength(double queueBeforeVehicleLength) {
        this.queueBeforeVehicleLength = queueBeforeVehicleLength;
    }
    
    
    
    
    
    

    public Vehicle(String id, SimulationNode position) {
        super(id, position);
    }

    @Override
    public void setPosition(SimulationNode position) {
        super.setPosition(position);
        if(driver != null){
            driver.setPosition(position);
        }
    }

    
}
