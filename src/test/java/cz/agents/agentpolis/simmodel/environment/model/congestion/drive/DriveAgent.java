/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.drive;

import cz.agents.agentpolis.siminfrastructure.description.DescriptionImpl;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.agentpolis.simmodel.environment.model.congestion.CongestionTestType;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
public class DriveAgent extends Agent implements Driver<PhysicalVehicle>{
    
    private DelayData delayData;
    
    private PhysicalVehicle drivenCar;
    
    private Node targetNode;

    public DriveAgent(String agentId, Node position) {
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
    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public Node getTargetNode() {
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
