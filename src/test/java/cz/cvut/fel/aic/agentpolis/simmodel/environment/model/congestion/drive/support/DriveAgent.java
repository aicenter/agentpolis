/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.CongestionTestType;

/**
 *
 * @author fido
 */
public class DriveAgent extends Agent implements Driver<PhysicalVehicle>{
    
    private DelayData delayData;
    
    private PhysicalVehicle drivenCar;
    
    private SimulationNode targetNode;

    public DriveAgent(String agentId, SimulationNode position) {
        super(agentId, position);
    }

    @Override
    public EntityType getType() {
        return CongestionTestType.TEST_DRIVER;
    }

    @Override
    public DescriptionImpl getDescription() {
        return null;
    }

    @Override
    public PhysicalVehicle getVehicle() {
        return drivenCar;
    }

    @Override
    public void startDriving(PhysicalVehicle vehicle){
        this.drivenCar = vehicle;
        vehicle.setDriver(this);
    }
    
    @Override
    public void endDriving(){
        drivenCar.setDriver(null);
        this.drivenCar = null;
    }

    

    @Override
    public void setTargetNode(SimulationNode targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public SimulationNode getTargetNode() {
        return targetNode;
    }

    @Override
    public void setDelayData(DelayData delayData) {
        this.delayData = delayData;
    }

    @Override
    public DelayData getDelayData() {
        return delayData;
    }



    @Override
    public double getVelocity() {
        return 15;
    }
    
}
