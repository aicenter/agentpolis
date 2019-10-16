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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author david
 */
@Singleton
public class AStarShortestPathPlanner implements ShortestPathPlanner{
	
	private final Map<Set<GraphType>, AStarShortestPath<SimulationNode, DefaultWeightedEdge>> 
			shortestPathPlannersMappedByGraphTypes;
	
	private final TransportNetworks transportNetworks;
	
	private final AStarAdmissibleHeuristic heuristic;

	
	@Inject
	public AStarShortestPathPlanner(TransportNetworks transportNetworks, AStarAdmissibleHeuristic heuristic) {
		shortestPathPlannersMappedByGraphTypes = new HashMap<>();
		this.transportNetworks = transportNetworks;
		this.heuristic = heuristic;
	}
	
	
	
	

	@Override
	public Trip<SimulationNode> findShortestPath(SimulationNode from, SimulationNode to, Set<GraphType> graphTypes) {
		if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
			createShortestPathPlanner(graphTypes);
		}
		AStarShortestPath<SimulationNode,DefaultWeightedEdge> planner 
				= shortestPathPlannersMappedByGraphTypes.get(graphTypes);
		GraphPath<SimulationNode,DefaultWeightedEdge> path = planner.getPath(from, to);
		return new Trip<>(path.getVertexList().toArray(SimulationNode[]::new));
	}


	private void createShortestPathPlanner(Set<GraphType> graphTypes) {
		DefaultDirectedWeightedGraph<SimulationNode, DefaultWeightedEdge> jGraphTGraph
				= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Set<Integer> addedNodes = new HashSet<>();

		for (GraphType graphType : graphTypes) {
			Graph<SimulationNode, SimulationEdge> graph = transportNetworks.getGraph(graphType);
			for (SimulationNode node : graph.getAllNodes()) {
				int fromPositionByNodeId = node.id;
				if (!addedNodes.contains(fromPositionByNodeId)) {
					addedNodes.add(fromPositionByNodeId);
					jGraphTGraph.addVertex(node);
				}

				for (SimulationEdge edge : graph.getOutEdges(node.id)) {
					Integer toPositionByNodeId = edge.toNode.getId();
					if (!addedNodes.contains(toPositionByNodeId)) {
						addedNodes.add(toPositionByNodeId);
						jGraphTGraph.addVertex(edge.toNode);
					}
					
					DefaultWeightedEdge newEdge = jGraphTGraph.addEdge(edge.fromNode, edge.toNode);
					long duration = Math.round((double) edge.getLengthCm() / edge.getAllowedMaxSpeedInCmPerSecond() * 1000);
					jGraphTGraph.setEdgeWeight(newEdge, duration);
				}

			}

		}
		
		AStarShortestPath<SimulationNode,DefaultWeightedEdge> jGraphTFinder 
				= new AStarShortestPath(jGraphTGraph, heuristic);
		
		shortestPathPlannersMappedByGraphTypes.put(graphTypes, jGraphTFinder);
	}
	
}
