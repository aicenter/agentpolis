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
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.CongestedDrive;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;


/**
 * @author fido
 */
@Singleton
public class CongestedDriveFactory extends ActivityFactory implements PhysicalVehicleDriveFactory {

	private final CongestionModel congestionModel;
	private final TripsUtil tripsUtil;

	@Inject
	public CongestedDriveFactory(CongestionModel congestionModel, TripsUtil tripsUtil) {
		this.congestionModel = congestionModel;
		this.tripsUtil = tripsUtil;
	}

	@Override
	public <A extends Agent & Driver> void runActivity(A agent, PhysicalVehicle vehicle,
													   Trip<SimulationNode> trip) {
		new CongestedDrive(activityInitializer, agent, congestionModel, trip, vehicle).run();
	}

	@Override
	public <A extends Agent & Driver> CongestedDrive create(A agent, PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
		return new CongestedDrive(activityInitializer, agent, congestionModel, trip, vehicle);

	}

	@Override
	public <A extends Agent & Driver> CongestedDrive create(A agent, PhysicalVehicle vehicle, SimulationNode target) {
		Trip<SimulationNode> trip = tripsUtil.createTrip(agent.getPosition(), target);
		return new CongestedDrive(activityInitializer, agent, congestionModel, trip,vehicle);
	}
}
