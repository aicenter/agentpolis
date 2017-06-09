/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.Message;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.congestion.CongestionMessage;
import cz.agents.agentpolis.simmodel.environment.model.congestion.CongestionModel;

/**
 *
 * @author fido
 * @param <A>
 */
public class CongestedDrive<A extends Agent & Driver<PhysicalVehicle>> extends Activity<A>{
    
    private final CongestionModel congestionModel;
    
    private final Trip<SimulationNode> trip;
    
    private final PhysicalVehicle vehicle;

    public CongestedDrive(ActivityInitializer activityInitializer, A agent, CongestionModel congestionModel, 
            Trip<SimulationNode> trip, PhysicalVehicle vehicle) {
        super(activityInitializer, agent);
        this.congestionModel = congestionModel;
        this.trip = trip;
        this.vehicle = vehicle;
    }

    @Override
    protected void performAction() {
        agent.startDriving(vehicle);
        congestionModel.drive(vehicle, trip);
    }

    @Override
    public void processMessage(Message message) {
        if(message.getType() == CongestionMessage.DRIVING_FINISHED){
            agent.endDriving();
            finish();
        }
    }
    
    
    
}
