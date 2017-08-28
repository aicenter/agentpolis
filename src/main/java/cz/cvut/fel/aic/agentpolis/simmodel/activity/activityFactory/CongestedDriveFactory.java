/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.CongestedDrive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;


/**
 * @author fido
 */
@Singleton
public class CongestedDriveFactory extends ActivityFactory implements PhysicalVehicleDriveFactory {

    private final CongestionModel congestionModel;

    @Inject
    public CongestedDriveFactory(CongestionModel congestionModel) {
        this.congestionModel = congestionModel;
    }

    @Override
    public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle,
                                                       Trip<SimulationNode> trip) {
        new CongestedDrive(activityInitializer, agent, congestionModel, trip, vehicle).run();
    }

    @Override
    public <A extends Agent & Driver> CongestedDrive create(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        return new CongestedDrive(activityInitializer, agent, congestionModel, trip, vehicle);

    }
}
