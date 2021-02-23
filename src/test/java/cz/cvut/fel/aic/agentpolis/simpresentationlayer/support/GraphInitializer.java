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
package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;
import java.util.Arrays;

public class GraphInitializer {

	private static int SRID = TestModule.testSRID();

	public static Graph<SimulationNode, SimulationEdge> getGraphForTest() {
		GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

		SimulationNode node0 = new SimulationNode(0, 0, GPSLocationTools.createGPSLocation(60.719220418076370,114.91617679595947,  0, SRID), 0);
		SimulationNode node1 = new SimulationNode(1, 0, GPSLocationTools.createGPSLocation(60.720637175426080,114.92497444152832,  0, SRID), 0);
		SimulationNode node2 = new SimulationNode(2, 0, GPSLocationTools.createGPSLocation(60.718811120988040,114.92615461349486,  0, SRID), 0);
		SimulationNode node3 = new SimulationNode(3, 0, GPSLocationTools.createGPSLocation(60.719598226145220,114.93063926696777,  0, SRID), 0);
		SimulationNode node4 = new SimulationNode(4, 0, GPSLocationTools.createGPSLocation(60.718170927906655,114.93164777755737,  0, SRID), 0);
		SimulationNode node5 = new SimulationNode(5, 0, GPSLocationTools.createGPSLocation(60.716050853194270,114.91836547851562,  0, SRID), 0);


		graphBuilder.addNode(node0);
		graphBuilder.addNode(node1);
		graphBuilder.addNode(node2);
		graphBuilder.addNode(node3);
		graphBuilder.addNode(node4);
		graphBuilder.addNode(node5);

		SimulationEdge edge0 = new SimulationEdge(node0, node1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)));
		SimulationEdge edge1 = new SimulationEdge(node1, node2, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)));
		SimulationEdge edge2 = new SimulationEdge(node2, node3, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node3)));
		SimulationEdge edge3 = new SimulationEdge(node3, node4, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node3, node4)));
		SimulationEdge edge4 = new SimulationEdge(node4, node5, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node4, node5)));
		SimulationEdge edge5 = new SimulationEdge(node5, node0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node5, node0)));


		graphBuilder.addEdge(edge0);
		graphBuilder.addEdge(edge1);
		graphBuilder.addEdge(edge2);
		graphBuilder.addEdge(edge3);
		graphBuilder.addEdge(edge4);
		graphBuilder.addEdge(edge5);

		return graphBuilder.createGraph();
	}

}
