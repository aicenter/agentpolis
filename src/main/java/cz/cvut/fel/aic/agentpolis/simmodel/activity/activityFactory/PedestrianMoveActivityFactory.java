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
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Move;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

/**
 * @author fido
 */
@Singleton
public class PedestrianMoveActivityFactory extends ActivityFactory {
	private final TypedSimulation eventProcessor;
	
	private final MoveUtil moveUtil;

	@Inject
	public PedestrianMoveActivityFactory(TypedSimulation eventProcessor,MoveUtil moveUtil) {
		this.eventProcessor = eventProcessor;
		this.moveUtil = moveUtil;
	}


	public <AG extends Agent & MovingAgent> Move<AG> create(
			AG agent, 
			SimulationEdge edge, 
			SimulationNode from, 
			SimulationNode to) {
		return new Move<>(activityInitializer, eventProcessor, agent, edge, from, to, moveUtil);
	}
}
