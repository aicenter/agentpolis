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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.Transformer;

import java.util.Arrays;

/**
 * @author fido
 */
public class Utils {
	
	private static final int DEFAULT_RADIUS = 5000;
	
	private static final int DEFAULT_GRID_SIZE = 5000; //in cm -> 50m
	
	public static Graph<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount, Transformer projection){
		return getCompleteGraph(nodeCount, projection, DEFAULT_RADIUS);
	}
	
	public static Graph<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount, Transformer projection, 
			int radius) {
		GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

		for (int i = 0; i < nodeCount; i++) {
			double angle = 2 * Math.PI / nodeCount * i;

			int x = (int) Math.round(radius * Math.cos(angle));
			int y = (int) Math.round(radius * Math.sin(angle));

                        
			SimulationNode node = new SimulationNode(i, 0, x, y, 0, projection, 0);

			graphBuilder.addNode(node);

			for (int j = 0; j < i; j++) {
				SimulationEdge edge1 = new SimulationEdge(graphBuilder.getNode(i), graphBuilder.getNode(j), 
						0, 0, DEFAULT_GRID_SIZE, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(i), graphBuilder.getNode(j))));
				SimulationEdge edge2 = new SimulationEdge(graphBuilder.getNode(j), graphBuilder.getNode(i), 
						0, 0, DEFAULT_GRID_SIZE, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(j), graphBuilder.getNode(i))));

				graphBuilder.addEdge(edge1);
				graphBuilder.addEdge(edge2);
			}
		}

		return graphBuilder.createGraph();
	}
	
	public static Graph<SimulationNode, SimulationEdge> getGridGraph(int width, Transformer projection) {
		return getGridGraph(width, projection, width);
	}
	
	public static Graph<SimulationNode, SimulationEdge> getGridGraph(int width, Transformer projection, 
			int height) {
		GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
		
		int size = DEFAULT_GRID_SIZE;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				int x = j * size;
				int y = i * size;
				int nodeId = i * width + j;
                                
				SimulationNode node = new SimulationNode(nodeId, 0, y, x, 0, projection, 0);
				graphBuilder.addNode(node);
				
				// bottom edges
				if(i != 0){
					int bottomNodeId = nodeId - width;
					createEdges(nodeId, bottomNodeId, graphBuilder);
				}
				
				// left edges
				if(j != 0){
					int leftNodeId = nodeId - 1;
					createEdges(nodeId, leftNodeId, graphBuilder);
				}
				
			}
		}

		return graphBuilder.createGraph();
	}
	
	private static void createEdges(int id1, int id2, GraphBuilder<SimulationNode, SimulationEdge> graphBuilder){
		SimulationEdge edge1 = new SimulationEdge(graphBuilder.getNode(id1), graphBuilder.getNode(id2), 0, 0, DEFAULT_GRID_SIZE, 
				40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(id1), graphBuilder.getNode(id2))));
		SimulationEdge edge2 = new SimulationEdge(graphBuilder.getNode(id2), graphBuilder.getNode(id1), 0, 0, DEFAULT_GRID_SIZE, 
				40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(id2), graphBuilder.getNode(id1))));

		graphBuilder.addEdge(edge1);
		graphBuilder.addEdge(edge2);
	}
}
