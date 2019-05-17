/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.entity;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.AliteEntity;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

/**
 * Entity for AgentPolis (agent or vehicle ...)
 *
 * @author Zbynek Moler
 */
public abstract class AgentPolisEntity extends AliteEntity {

	private final String id;

	private SimulationNode position;

	public SimulationNode getPosition() {
		return position;
	}

	public void setPosition(SimulationNode position) {
		this.position = position;
	}

	public AgentPolisEntity(String id, SimulationNode position) {
		this.id = id;
		this.position = position;
	}

	@Inject
	@Override
	public void init(TypedSimulation eventProcessor) {
		super.init(eventProcessor);
	}


	public abstract EntityType getType();

	public String getId() {
		return id;
	}

}
