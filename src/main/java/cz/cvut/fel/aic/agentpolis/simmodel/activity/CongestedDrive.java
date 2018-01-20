/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CTMCongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;

/**
 *
 * @author fido
 * @param <A>
 */
public class CongestedDrive<A extends Agent & Driver<PhysicalVehicle>> extends Activity<A> implements PhysicalVehicleDrive{
    
    private final CTMCongestionModel congestionModel;
    
    private final Trip<SimulationNode> trip;
    
    private final PhysicalVehicle vehicle;

    public CongestedDrive(ActivityInitializer activityInitializer, A agent, CTMCongestionModel congestionModel,
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
