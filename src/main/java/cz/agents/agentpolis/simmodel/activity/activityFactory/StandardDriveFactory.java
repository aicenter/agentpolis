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
import cz.agents.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.agents.agentpolis.simmodel.ActivityFactory;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.IdGenerator;
import cz.agents.agentpolis.simmodel.activity.Drive;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Node;

/**
 * @author fido
 */
@Singleton
public class StandardDriveFactory extends ActivityFactory implements PhysicalVehicleDriveFactory {

    private final TransportNetworks transportNetworks;

    private final VehicleMoveActivityFactory moveActivityFactory;

    private final TypedSimulation eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final IdGenerator tripIdGenerator;

    private final TripsUtil tripsUtil;


    @Inject
    public StandardDriveFactory(TransportNetworks transportNetworks, VehicleMoveActivityFactory moveActivityFactory,
                                TypedSimulation eventProcessor, StandardTimeProvider timeProvider, IdGenerator tripIdGenerator,
                                TripsUtil tripsUtil) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripIdGenerator = tripIdGenerator;
        this.tripsUtil = tripsUtil;
    }


    @Override
    public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        create(agent, vehicle, trip).run();
    }


    public <A extends Agent & Driver> Drive<A> create(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor,
                timeProvider, agent, vehicle, trip, tripIdGenerator.getId());
    }

    public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, Node targetPosition) {
        Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition().getId(), targetPosition.getId());
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider,
                agent, vehicle, trip, tripIdGenerator.getId());
    }
}
