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

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;


public abstract class MapInitializer {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapInitializer.class);

	protected final AgentpolisConfig config;


	
	public MapInitializer(AgentpolisConfig config) {
		this.config = config;
	}


	/**
	 * init map
	 *
	 * @return map data with simulation graph
	 */
	public MapData getMap() {
		Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
		graphs.put(EGraphType.HIGHWAY, getGraph());

		Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

		LOGGER.info("Graphs imported, highway graph details: {}", graphs.get(EGraphType.HIGHWAY));
		return new MapData(graphs, nodes);
	}
	
	protected abstract Graph<SimulationNode, SimulationEdge> getGraph();

	/**
	 * Build map data
	 */
	private Map<Integer, SimulationNode> createAllGraphNodes(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType) {

		Map<Integer, SimulationNode> nodesFromAllGraphs = new HashMap<>();

		for (GraphType graphType : graphByGraphType.keySet()) {
			Graph<SimulationNode, SimulationEdge> graphStorageTmp = graphByGraphType.get(graphType);
			for (SimulationNode node : graphStorageTmp.getAllNodes()) {
				nodesFromAllGraphs.put(node.getId(), node);
			}

		}

		return nodesFromAllGraphs;

	}
}
