/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Drive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import org.slf4j.LoggerFactory;

/**
 * @author fido
 */
@Singleton
public class DriveActivityFactory extends ActivityFactory {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DriveActivityFactory.class);
	
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
				LOGGER.warn("Trying to drive vehicle that is being transported by other vehicle!");
			}
		}
	}

	public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, SimulationNode targetPosition) {
		vehicleCheck(vehicle);
		Trip<SimulationNode> trip = tripsUtil.createTrip(vehicle.getPosition().getId(), targetPosition.getId());
		return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, vehicle, trip,
				tripIdGenerator.getId());
	}
}
