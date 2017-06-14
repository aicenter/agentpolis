/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.agents.agentpolis.simmodel.ActivityFactory;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.IdGenerator;
import cz.agents.agentpolis.simmodel.activity.Drive;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Node;

/**
 * @author fido
 */
@Singleton
public class DriveActivityFactory extends ActivityFactory {

    private final TransportNetworks transportNetworks;

    private final VehicleMoveActivityFactory moveActivityFactory;

    private final TypedSimulation eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final IdGenerator tripIdGenerator;

    private final TripsUtil tripsUtil;


    @Inject
    public DriveActivityFactory(TransportNetworks transportNetworks, VehicleMoveActivityFactory moveActivityFactory,
                                TypedSimulation eventProcessor, StandardTimeProvider timeProvider, IdGenerator tripIdGenerator,
                                TripsUtil tripsUtil) {
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripIdGenerator = tripIdGenerator;
        this.tripsUtil = tripsUtil;
    }


    public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, Trip<Node> trip) {
        vehicleCheck(vehicle);
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, vehicle, trip,
                tripIdGenerator.getId());
    }

    private void vehicleCheck(Vehicle vehicle) {
        if (vehicle instanceof TransportableEntity) {
            if (((TransportableEntity) vehicle).getTransportingEntity() != null) {
                Log.warn(this, "Trying to drive vehicle that is being transported by other vehicle!");
                System.exit(-1);
            }
        }
    }

    public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, Node targetPosition) {
        vehicleCheck(vehicle);
        Trip<Node> trip = tripsUtil.createTrip(agent.getPosition().getId(), targetPosition.getId());
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, vehicle, trip,
                tripIdGenerator.getId());
    }
}
