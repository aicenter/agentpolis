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


import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import java.util.List;

/**
 * @param <A>
 * @author fido
 */
public class VehicleMove<A extends Agent & Driver> extends Move<A> {

	public VehicleMove(ActivityInitializer activityInitializer,
					   TypedSimulation eventProcessor, 
					   A agent, 
					   SimulationEdge edge, 
					   SimulationNode from,
					   SimulationNode to,
					   MoveUtil moveUtil) {
		super(activityInitializer, eventProcessor, agent, edge, from, to, moveUtil);

	}

	@Override
	protected void performAction() {
		if (agent instanceof Driver && agent.getVehicle() != null) {
			moveVehicle(agent.getVehicle());
		}
		super.performAction();
	}

	private void moveVehicle(Vehicle vehicle) {
		vehicle.setPosition(to);
		if (vehicle instanceof TransportEntity && !((TransportEntity) vehicle).getTransportedEntities().isEmpty()) {
			moveTransportedEntities(((TransportEntity) vehicle).getTransportedEntities());
		}
	}

	private void moveTransportedEntities(List<TransportableEntity> transportedEntities) {
		for (TransportableEntity transportedEntity : transportedEntities) {
			transportedEntity.setPosition(to);
		}
	}


}
