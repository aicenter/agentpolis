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
package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.VehicleMoveActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.DriveEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.Transit;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.geographtools.Graph;


/**
 * @param <A>
 * @author fido
 */
public class Drive<A extends Agent & Driver> extends PhysicalVehicleDrive<A> {

	private final Vehicle vehicle;

	private final Trip<SimulationNode> trip;

	private final VehicleMoveActivityFactory moveActivityFactory;

	private final Graph<SimulationNode, SimulationEdge> graph;

	private final EventProcessor eventProcessor;

	private final StandardTimeProvider timeProvider;


	private SimulationNode from;

	private SimulationNode to;
	
	
	

	public Drive(ActivityInitializer activityInitializer, TransportNetworks transportNetworks,
				 VehicleMoveActivityFactory moveActivityFactory, TypedSimulation eventProcessor, 
				 StandardTimeProvider timeProvider,
				 A agent, Vehicle vehicle, Trip<SimulationNode> trip) {
		super(activityInitializer, agent);
		this.vehicle = vehicle;
		this.trip = trip;
		this.moveActivityFactory = moveActivityFactory;
		this.eventProcessor = eventProcessor;
		this.timeProvider = timeProvider;
		graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
	}

	@Override
	protected void performAction() {
		agent.startDriving(vehicle);
		from = trip.removeFirstLocation();
		move();
	}

	@Override
	protected void onChildActivityFinish(Activity activity) {
		if (trip.isEmpty() || stoped) {
			agent.endDriving();
			vehicle.setLastFromPosition(from);
			finish();
		} else {
			from = to;
			move();
		}
	}

	private void move() {
		to = trip.removeFirstLocation();
		SimulationEdge edge = graph.getEdge(from, to);

		runChildActivity(moveActivityFactory.create(agent, edge, from, to));
		triggerVehicleEnteredEdgeEvent();
	}

	private void triggerVehicleEnteredEdgeEvent() {
		SimulationEdge edge = graph.getEdge(from, to);
		Transit transit = new Transit(timeProvider.getCurrentSimTime(), edge.getStaticId(),trip.getTripId(), agent);
		eventProcessor.addEvent(DriveEvent.VEHICLE_ENTERED_EDGE, null, null, transit);
	}

	public Trip<SimulationNode> getTrip() {
		return trip;
	}
}
