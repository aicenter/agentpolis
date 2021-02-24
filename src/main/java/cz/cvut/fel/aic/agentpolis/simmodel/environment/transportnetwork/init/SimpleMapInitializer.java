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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

/**
 *
 * @author David Fiedler
 */
@Singleton
public class SimpleMapInitializer extends MapInitializer{
	
	private Graph<SimulationNode, SimulationEdge> graph;

	
	
	
	public void setGraph(Graph<SimulationNode, SimulationEdge> graph) {
		this.graph = graph;
	}
	
	
	

	@Inject
	public SimpleMapInitializer(AgentpolisConfig config) {
		super(config);
	}

	@Override
	protected Graph<SimulationNode, SimulationEdge> getGraph() {
		return graph;
	}
	
	
	
//	public interface SimpleMapInitializerFactory {
//		public SimpleMapInitializer create(Graph<SimulationNode, SimulationEdge> graph);
//	}
	
}
