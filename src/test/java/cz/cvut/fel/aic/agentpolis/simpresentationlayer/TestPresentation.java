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
package cz.cvut.fel.aic.agentpolis.simpresentationlayer;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simpresentationlayer.support.GraphInitializer;
import cz.cvut.fel.aic.agentpolis.simpresentationlayer.support.TestModule;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TestPresentation {

	@Test
	public void run() {
		Graph<SimulationNode, SimulationEdge> graph = GraphInitializer.getGraphForTest();

		AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(new TestModule());
		Injector injector = agentPolisInitializer.initialize();

		injector.getInstance(AgentpolisConfig.class).visio.showVisio = VisualTests.SHOW_VISIO;
		
		SimulationCreator creator = injector.getInstance(SimulationCreator.class);

		creator.prepareSimulation(getMapData(graph));

		creator.startSimulation();
	}



	private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
		Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
		graphs.put(EGraphType.HIGHWAY, graph);

		Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

		return new MapData(graphs, nodes);
	}


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

	public static void main(String[] args) {
		VisualTests.runVisualTest(TestPresentation.class);
	}
}

