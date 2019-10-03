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
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

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

	@Override
	public <A extends Agent & Driver> Drive<A> create(A agent, PhysicalVehicle vehicle, SimulationNode target) {
		Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition(), target);

		return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider,
				agent, vehicle, trip, tripIdGenerator.getId());
	}

//	public <AG extends Agent & Driver> Drive<AG> create(AG agent, Vehicle vehicle, Node targetPosition) {
//		Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition().getId(), targetPosition.getId());
//		return new Drive<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider,
//				agent, vehicle, trip, tripIdGenerator.getId());
//	}
}
