/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Drive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;

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


    public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, Trip<SimulationNode> trip) {
        vehicleCheck(vehicle);
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, vehicle, trip,
                tripIdGenerator.getId());
    }

    private void vehicleCheck(Vehicle vehicle) {
        if (vehicle instanceof TransportableEntity) {
            if (((TransportableEntity) vehicle).getTransportingEntity() != null) {
                Log.warn(this, "Trying to drive vehicle that is being transported by other vehicle!");
            }
        }
    }

    public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, SimulationNode targetPosition) {
        vehicleCheck(vehicle);
        Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition().getId(), targetPosition.getId());
        return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, vehicle, trip,
                tripIdGenerator.getId());
    }
}
