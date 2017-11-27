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

public abstract class Vehicle extends AgentPolisEntity implements MovingEntity {
    private Driver driver;
    private double queueBeforeVehicleLength;
    private SimulationNode lastFromPosition;
    private long laneId = -1; // lana id == congestion lane and lane id

    /**
     * Vehicle
     *
     * @param id
     * @param position
     */
    public Vehicle(String id, SimulationNode position) {
        super(id, position);
    }

    @Override
    public SimulationNode getTargetNode() {
        return ((driver != null) ? driver.getTargetNode() : null);
    }

    @Override
    public DelayData getDelayData() {
        return ((driver != null) ? driver.getDelayData() : null);
    }

    @Override
    public void setPosition(SimulationNode position) {
        super.setPosition(position);
        if (driver != null) {
            driver.setPosition(position);
        }
    }

    public SimulationNode getLastFromPosition() {
        return lastFromPosition;
    }

    public void setLastFromPosition(SimulationNode lastTargetPosition) {
        this.lastFromPosition = lastTargetPosition;
    }

    /**
     * Que
     *
     * @return
     */
    public double getQueueBeforeVehicleLength() {
        return queueBeforeVehicleLength;
    }

    public void setQueueBeforeVehicleLength(double queueBeforeVehicleLength) {
        this.queueBeforeVehicleLength = queueBeforeVehicleLength;
    }

    /**
     * Driver
     */
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * ID of lane where the vehicle is
     *
     * @param id - lane id or congestion lane id, laneId == congestionlaneId
     */
    public void setLaneId(long id) {
        this.laneId = id;
    }

    /**
     * @return unique id, it is possible to retrieve lane or congestionLane. -1 for unknown situation.
     */
    public long getLaneId() {
        return this.laneId;
    }


}
