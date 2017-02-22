/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.Drive;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
@Singleton
public class DriveActivityFactory {

    private final TransportNetworks transportNetworks;
    private final MoveActivityFactory moveActivityFactory;


    @Inject
    public DriveActivityFactory(TransportNetworks transportNetworks, MoveActivityFactory moveActivityFactory) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
    }



    public <AG extends Agent & TransportAgent> Drive<AG> create(AG agent, Vehicle vehicle, Trip<Node> trip){
        return new Drive<>(transportNetworks, moveActivityFactory, agent, vehicle, trip);
    }
}