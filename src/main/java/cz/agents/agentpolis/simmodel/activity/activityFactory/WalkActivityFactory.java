/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.ActivityFactory;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.IdGenerator;
import cz.agents.agentpolis.simmodel.activity.Walk;
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Node;

/**
 * @author schaemar
 */
@Singleton
public class WalkActivityFactory extends ActivityFactory {

    private final TransportNetworks transportNetworks;

    private final PedestrianMoveActivityFactory moveActivityFactory;

    private final TypedSimulation eventProcessor;

    private final TimeProvider timeProvider;

    private final IdGenerator tripIdGenerator;

    private final TripsUtil tripsUtil;


    @Inject
    public WalkActivityFactory(TransportNetworks transportNetworks, PedestrianMoveActivityFactory moveActivityFactory,
                               TypedSimulation eventProcessor, TimeProvider timeProvider, IdGenerator tripIdGenerator,
                               TripsUtil tripsUtil) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripIdGenerator = tripIdGenerator;
        this.tripsUtil = tripsUtil;
    }


    public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, Trip<Node> trip) {
        return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip,
                tripIdGenerator.getId());
    }

    public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, Node targetPosition) {
        Trip<Node> trip = tripsUtil.createTrip(agent.getPosition().getId(), targetPosition.getId());
        return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip,
                tripIdGenerator.getId());
    }
}
