/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * @author fido
 */
public abstract class Vehicle extends AgentPolisEntity implements MovingEntity {

    private Driver driver;

    private double queueBeforeVehicleLength;

    private SimulationNode lastFromPosition;

    public SimulationNode getLastFromPosition() {
        return lastFromPosition;
    }

    @Override
    public SimulationNode getTargetNode() {
        return ((driver != null) ? driver.getTargetNode() : null);
    }

    @Override
    public DelayData getDelayData() {
        return ((driver != null) ? driver.getDelayData() : null);
    }

    public void setLastFromPosition(SimulationNode lastTargetPosition) {
        this.lastFromPosition = lastTargetPosition;
    }


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
        if (driver != null) {
            driver.setPosition(position);
        }
    }


}
