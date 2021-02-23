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
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.TestModule;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.MapDataGenerator;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fido
 */
public class VisioTest {
	
	public void run(Graph<SimulationNode, SimulationEdge> graph){
		
		// Guice configuration
		AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(
				new TestModule());
//		agentPolisInitializer.overrideModule(new TestModule());
		Injector injector = agentPolisInitializer.initialize();

		SimulationCreator creator = injector.getInstance(SimulationCreator.class);

		// prepare map, entity storages...
		creator.prepareSimulation(getMapData(graph));
		
		creator.startSimulation();
	}

	private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
		Map<GraphType,Graph<SimulationNode,SimulationEdge>> graphs = new HashMap<>();
		graphs.put(EGraphType.HIGHWAY, graph);

		return MapDataGenerator.getMap(graphs);
	}
}
