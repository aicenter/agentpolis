/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.MovingEntity;

/**
 *
 * @author fido
 */
public abstract class Vehicle extends AgentPolisEntity implements MovingEntity{
    
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    

    public Vehicle(String id) {
        super(id);
    }

    
}
