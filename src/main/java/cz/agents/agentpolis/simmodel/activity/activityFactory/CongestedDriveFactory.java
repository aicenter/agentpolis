/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.ActivityFactory;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.CongestedDrive;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.congestion.CongestionModel;
import cz.agents.basestructures.Node;
import javax.inject.Inject;

/**
 *
 * @author fido
 */
@Singleton
public class CongestedDriveFactory extends ActivityFactory implements PhysicalVehicleDriveFactory{
    
    private final CongestionModel congestionModel;

    @Inject
    public CongestedDriveFactory(CongestionModel congestionModel) {
        this.congestionModel = congestionModel;
    }
    
    @Override
    public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle, 
            Trip<SimulationNode> trip){
        new CongestedDrive(activityInitializer, agent, congestionModel, trip, vehicle).run();
    }
}
