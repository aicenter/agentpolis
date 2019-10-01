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
package cz.cvut.fel.aic.agentpolis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.AStarShortestPath.Heuristic;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.MapInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.utils.Benchmark;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.slf4j.LoggerFactory;

/**
 *
 * @author david
 */
public class AStarBenchmark {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AStarBenchmark.class);
	
	
	public static void main(String[] args) {
		Module module = new LiteModule();
		Injector injector = Guice.createInjector(module);

//		injector.getInstance(AgentpolisConfig.class).visio.showVisio = true;
		injector.getInstance(AgentpolisConfig.class).mapNodesFilepath 
				= "C:/AIC data/Shared/amod-data/VGA Evaluation/maps/nodes.geojson";
		injector.getInstance(AgentpolisConfig.class).mapEdgesFilepath 
				= "C:/AIC data/Shared/amod-data/VGA Evaluation/maps/edges.geojson";


		// prepare map
		MapData mapData = injector.getInstance(MapInitializer.class).getMap();

		Graph<SimulationNode,SimulationEdge> graph = mapData.graphByType.get(EGraphType.HIGHWAY);
		
		AStarAdmissibleHeuristic heuristic = injector.getInstance(EuclideanTraveltimeHeuristic.class);
		
		DefaultDirectedWeightedGraph<SimulationNode,DefaultWeightedEdge> jGraphTGraph = createJGraphTGraph(graph);
		
		// for jGraphT Astar
		AStarShortestPath jGraphTFinder = new AStarShortestPath(jGraphTGraph, heuristic);
		
		// for jGraphT Dijkstra
		DijkstraShortestPath<SimulationNode,DefaultWeightedEdge> dijkstraFinder
				= new DijkstraShortestPath(jGraphTGraph);
		
		// random pairs of nodes
		Random r = new Random(5);
		int max = graph.getAllNodes().size();
		int sampleCount = 1000;
		Set<Integer> fromInicies = new HashSet<>();
		Set<Integer> toInicies = new HashSet<>();
		for (int i = 0; i < sampleCount; i++) {
			int nextFrom = r.nextInt(max);
			while (toInicies.contains(nextFrom) || fromInicies.contains(nextFrom)) {
				nextFrom = r.nextInt(max);
			}
			fromInicies.add(nextFrom);
			
			int nextTo = r.nextInt(max);
			while (toInicies.contains(nextTo) || fromInicies.contains(nextTo)) {
				nextTo = r.nextInt(max);
			}
			toInicies.add(nextTo);
			
		}
		List<SimulationNode> fromNodes = new ArrayList<SimulationNode>(sampleCount);
		List<SimulationNode> toNodes = new ArrayList<SimulationNode>(sampleCount);
		int index = 0;
		for(SimulationNode node: graph.getAllNodes()){
			if(fromInicies.contains(index)){
				fromNodes.add(node);
			}
			else if(toInicies.contains(index)){
				toNodes.add(node);
			}
			index++;
		}
		
		LOGGER.info("Benchmarking JGraphT AStar");
		Benchmark jGraphTBenchmark = new Benchmark();
		jGraphTBenchmark.measureTime(() ->
				benchmarkJGraphT(sampleCount, fromNodes, toNodes, jGraphTFinder));
		int jGraphTTime = jGraphTBenchmark.getDurationMsInt();
		
		LOGGER.info("Benchmarking MC AStar");
		Benchmark mcBenchmark = new Benchmark();
		mcBenchmark.measureTime(() ->
				benchmarkMC(sampleCount, fromNodes, toNodes, jGraphTGraph, (Heuristic) heuristic));
		int mcTime = mcBenchmark.getDurationMsInt();
		
		LOGGER.info("Benchmarking Dijkstra");
		Benchmark currentBenchmark = new Benchmark();
		currentBenchmark.measureTime(() ->
				benchmarkDijkstra(sampleCount, fromNodes, toNodes, dijkstraFinder));
		int currentTime = currentBenchmark.getDurationMsInt();
		
		// tests
//		long jGraphTTotalTime = 0;
//		long mcTotalTime = 0;
//		long currentTotalTime = 0;
//		for (int i = 0; i < sampleCount; i++) {
//			SimulationNode origin = fromNodes.get(i);
//			SimulationNode destination = toNodes.get(i);
			
//			Benchmark jGraphTBenchmark = new Benchmark();
//			int jGraphTlength = jGraphTBenchmark.measureTime(() ->
//					computeShortestPathUsingJgraphT(jGraphTFinder, origin, destination));
//			int jGraphTTime = jGraphTBenchmark.getDurationMsInt();

//			Benchmark mcBenchmark = new Benchmark();
//			int mclength = jGraphTBenchmark.measureTime(() ->
//					computeShortestPathUsingMC(jGraphTGraph, origin, destination, (Heuristic) heuristic));
//			int mcTime = mcBenchmark.getDurationMsInt();

//			Benchmark currentBenchmark = new Benchmark();
//			int currentLength = jGraphTBenchmark.measureTime(() ->
//					computeShortestPathCurrent(dijkstraFinder, origin, destination));
//			int currentTime = currentBenchmark.getDurationMsInt();
			 
//			jGraphTTotalTime += jGraphTTime;
//			mcTotalTime += mcTime;
//			currentTotalTime += currentTime;
//		}
		System.out.println(String.format("Total time JGraphT: %s ms", jGraphTTime));
		System.out.println(String.format("Total time MC Astar: %s ms", mcTime));
		System.out.println(String.format("Total time current solution: %s ms", currentTime));
	}
	
	public static void benchmarkJGraphT(int sampleCount, List<SimulationNode> fromNodes, List<SimulationNode> toNodes,
			AStarShortestPath jGraphTFinder){
		for (int i = 0; i < sampleCount; i++) {
			SimulationNode origin = fromNodes.get(i);
			SimulationNode destination = toNodes.get(i);
			
			int jGraphTlength = computeShortestPathUsingJgraphT(jGraphTFinder, origin, destination);
		}
	}
	
	public static void benchmarkMC(int sampleCount, List<SimulationNode> fromNodes, List<SimulationNode> toNodes,
			DefaultDirectedWeightedGraph<SimulationNode,DefaultWeightedEdge> jGraphTGraph, Heuristic heuristic){
		for (int i = 0; i < sampleCount; i++) {
			SimulationNode origin = fromNodes.get(i);
			SimulationNode destination = toNodes.get(i);
			
			int mclength = computeShortestPathUsingMC(jGraphTGraph, origin, destination, heuristic);
		}
	}
	
	public static void benchmarkDijkstra(int sampleCount, List<SimulationNode> fromNodes, List<SimulationNode> toNodes,
			DijkstraShortestPath<SimulationNode,DefaultWeightedEdge> dijkstraFinder){
		for (int i = 0; i < sampleCount; i++) {
			SimulationNode origin = fromNodes.get(i);
			SimulationNode destination = toNodes.get(i);
			
			int jGraphTlength = computeShortestPathCurrent(dijkstraFinder, origin, destination);
		}
	}
	
	public static int computeShortestPathUsingMC(
			DefaultDirectedWeightedGraph<SimulationNode,DefaultWeightedEdge> jGraphTGraph, SimulationNode origin, 
			SimulationNode destination, Heuristic heuristic){
		cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.AStarShortestPath sPath = 
				new cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.AStarShortestPath(
						jGraphTGraph, origin, destination, heuristic);
		return (int) sPath.getPathLength();
	}
	
	public static int computeShortestPathUsingJgraphT(AStarShortestPath jGraphTFinder, SimulationNode origin, 
			SimulationNode destination){
		GraphPath<SimulationNode,SimulationEdge> path = jGraphTFinder.getPath(origin, destination);
		return (int) path.getWeight();
	}
	
	public static int computeShortestPathCurrent(
			DijkstraShortestPath<SimulationNode,DefaultWeightedEdge> finder, SimulationNode origin, 
			SimulationNode destination){
		GraphPath<SimulationNode,DefaultWeightedEdge> path = finder.getPath(origin, destination);
		return (int) path.getWeight();
	}

	private static DefaultDirectedWeightedGraph<SimulationNode,DefaultWeightedEdge> createJGraphTGraph(
			Graph<SimulationNode, SimulationEdge> graph) {
		DefaultDirectedWeightedGraph<SimulationNode,DefaultWeightedEdge> jGraphTGraph 
				= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
				
		for(SimulationNode node: graph.getAllNodes()){
			jGraphTGraph.addVertex(node);
		}
		
		for(SimulationEdge edge: graph.getAllEdges()){
			DefaultWeightedEdge newEdge = jGraphTGraph.addEdge(edge.fromNode, edge.toNode);
			long duration = Math.round((double) edge.getLengthCm() / edge.getAllowedMaxSpeedInCmPerSecond() * 1000);
			jGraphTGraph.setEdgeWeight(newEdge, duration);
		}
		
		return jGraphTGraph;
	}
	
}
