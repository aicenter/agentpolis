/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Walk;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.geographtools.Node;

/**
 * @author schaemar
 */
@Singleton
public class WalkActivityFactory extends ActivityFactory {

    private final TransportNetworks transportNetworks;

    private final PedestrianMoveActivityFactory moveActivityFactory;

    private final TypedSimulation eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final IdGenerator tripIdGenerator;

    private final TripsUtil tripsUtil;


    @Inject
    public WalkActivityFactory(TransportNetworks transportNetworks, PedestrianMoveActivityFactory moveActivityFactory,
                               TypedSimulation eventProcessor, StandardTimeProvider timeProvider, IdGenerator tripIdGenerator,
                               TripsUtil tripsUtil) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripIdGenerator = tripIdGenerator;
        this.tripsUtil = tripsUtil;
    }


    public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, Trip<SimulationNode> trip) {
        return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip,
                tripIdGenerator.getId());
    }

    public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, Node targetPosition) {
        Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition(), targetPosition, EGraphType.PEDESTRIAN);
        return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip,
                tripIdGenerator.getId());
    }
}
