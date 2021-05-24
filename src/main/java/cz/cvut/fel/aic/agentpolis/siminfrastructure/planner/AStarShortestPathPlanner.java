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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.Graph;
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
	
	public static class VehicleType implements EntityType{

		@Override
		public String getDescriptionEntityType() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
		
	}
	
	private final Map<Set<GraphType>, DefaultDirectedWeightedGraph<SimulationNode, DefaultWeightedEdge>> 
			shortestPathPlannersMappedByGraphTypes;
	
	private final TransportNetworks transportNetworks;
	
	private final AStarAdmissibleHeuristic heuristic;
	
	private final AgentpolisConfig config;
	
	private final MoveUtil moveUtil;

	
	@Inject
	public AStarShortestPathPlanner(
			TransportNetworks transportNetworks, 
			AStarAdmissibleHeuristic heuristic,
			AgentpolisConfig config,
			MoveUtil moveUtil) {
		shortestPathPlannersMappedByGraphTypes = new HashMap<>();
		this.transportNetworks = transportNetworks;
		this.heuristic = heuristic;
		this.config = config;
		this.moveUtil = moveUtil;
	}
	
	
	
	

	@Override
	public SimulationNode[] findShortestPath(SimulationNode from, SimulationNode to, Set<GraphType> graphTypes) {
		if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
			createShortestPathPlanner(graphTypes);
		}
		DefaultDirectedWeightedGraph<SimulationNode, DefaultWeightedEdge> Graph = 
                        shortestPathPlannersMappedByGraphTypes.get(graphTypes);
		AStarShortestPath<SimulationNode,DefaultWeightedEdge> planner 
				= new AStarShortestPath(Graph, heuristic);
		GraphPath<SimulationNode,DefaultWeightedEdge> path = planner.getPath(from, to);
		SimulationNode[] locations = path.getVertexList().stream().toArray(SimulationNode[]::new);
		return locations;
	}


	private void createShortestPathPlanner(Set<GraphType> graphTypes) {
		DefaultDirectedWeightedGraph<SimulationNode, DefaultWeightedEdge> jGraphTGraph
				= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Set<Integer> addedNodes = new HashSet<>();
		
		Vehicle referenceVehicle = new PhysicalVehicle(
				"Test vehicle", 
				new AStarShortestPathPlanner.VehicleType(), 
				4, 
				EGraphType.HIGHWAY, 
				null,
				config.maxVehicleSpeedInMeters);

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
					if (!addedNodes.contains(toPositionByNodeId)) { //not adding vertex, that is contained
						addedNodes.add(toPositionByNodeId);
						jGraphTGraph.addVertex(edge.toNode);
					}
                                        
                    DefaultWeightedEdge newEdge = jGraphTGraph.addEdge(edge.fromNode, edge.toNode);
					long travelTime;
					if(graphType == EGraphType.HIGHWAY){
						travelTime = moveUtil.computeDuration(referenceVehicle, edge);
					}
					else{
						travelTime = MoveUtil.computeMinDuration(edge);
					}
//					long duration = Math.round((double) edge.getLengthCm() / edge.getAllowedMaxSpeedInCmPerSecond() * 1000);
					jGraphTGraph.setEdgeWeight(newEdge, travelTime);
				}
			}
		}
		
		shortestPathPlannersMappedByGraphTypes.put(graphTypes, jGraphTGraph);
	}
	
}
