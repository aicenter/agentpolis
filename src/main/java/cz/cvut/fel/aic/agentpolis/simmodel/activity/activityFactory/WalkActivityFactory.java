/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Walk;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

/**
 * @author schaemar
 */
@Singleton
public class WalkActivityFactory extends ActivityFactory {

	private final TransportNetworks transportNetworks;

	private final PedestrianMoveActivityFactory moveActivityFactory;

	private final TypedSimulation eventProcessor;

	private final StandardTimeProvider timeProvider;

	private final TripsUtil tripsUtil;


	@Inject
	public WalkActivityFactory(TransportNetworks transportNetworks, PedestrianMoveActivityFactory moveActivityFactory,
							   TypedSimulation eventProcessor, StandardTimeProvider timeProvider,TripsUtil tripsUtil) {
		this.transportNetworks = transportNetworks;
		this.moveActivityFactory = moveActivityFactory;
		this.eventProcessor = eventProcessor;
		this.timeProvider = timeProvider;
		this.tripsUtil = tripsUtil;
	}


	public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, Trip<SimulationNode> trip) {
		return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip);
	}

	public <AG extends Agent & MovingAgent> Walk<AG> create(AG agent, SimulationNode targetPosition) {
		Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition(), targetPosition, EGraphType.PEDESTRIAN);
		return new Walk<>(activityInitializer, transportNetworks, moveActivityFactory, eventProcessor, timeProvider, agent, trip);
	}
}
