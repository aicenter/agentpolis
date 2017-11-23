/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphPathImpl;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.specific.Specifics;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.specific.SpecificsFactory;

/**
 * An implementation of <a href="http://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm</a>.
 *
 * @author Michal Cap
 * @since Apr 16, 2012
 */
public final class AStarShortestPath<V, E> {

	public static interface Heuristic<V> {

		double getHeuristicEstimate(V current, V goal);
	}

	//~ Instance fields --------------------------------------------------------

	Set<V> closed = new HashSet<>();

	Map<V, Double> gScores = new HashMap<>();
	Map<V, Double> hScores = new HashMap<>();
	Map<V, Double> fScores = new HashMap<>();

	Specifics<V, E> specifics;

	PriorityQueue<V> open = new PriorityQueue<>(100, new Comparator<V>() {

		@Override
		public int compare(V o1, V o2) {
			return (int) Math.signum(fScores.get(o1) - fScores.get(o2));
		}
	});

	Map<V, E> cameFrom = new HashMap<>();

	private Graph<V, E> graph;
	private GraphPath<V, E> path;

	//~ Constructors -----------------------------------------------------------

	/**
	 * Creates and executes a new AStarShortestPath algorithm instance. An instance is only good for a single search;
	 * after construction, it can be accessed to retrieve information about the path found.
	 *
	 * @param graph
	 * 		the graph to be searched
	 * @param startVertex
	 * 		the vertex at which the path should start
	 * @param endVertex
	 * 		the vertex at which the path should end
	 */
	public AStarShortestPath(Graph<V, E> graph, V startVertex, V endVertex, Heuristic<V> h) {
		this.graph = graph;

		if (!graph.containsVertex(startVertex)) {
			throw new IllegalArgumentException("graph must contain the start vertex - " + startVertex);
		}

		if (!graph.containsVertex(endVertex)) {
			throw new IllegalArgumentException("graph must contain the end vertex - " + endVertex);
		}

		specifics = SpecificsFactory.create(graph);

		open.add(startVertex);

		double hScoreStart = h.getHeuristicEstimate(startVertex, endVertex);
		gScores.put(startVertex, 0.0);
		hScores.put(startVertex, hScoreStart);
		fScores.put(startVertex, hScoreStart);

		while (!open.isEmpty()) {
			V current = open.poll();

			if (current.equals(endVertex)) {
				List<E> edgeList = reconstructEdgeList(startVertex, endVertex);

				double length = 0.0;
				for (E edge : edgeList) {
					length += graph.getEdgeWeight(edge);
				}

				path = new GraphPathImpl<>(graph, startVertex, endVertex, edgeList, length);

				return;
			}

			closed.add(current);

			Set<? extends E> neighborEdges = specifics.edgesOf(current);

			for (E edge : neighborEdges) {
				V next;
				if (graph.getEdgeSource(edge).equals(current)) {
					next = graph.getEdgeTarget(edge);
				} else if (graph.getEdgeTarget(edge).equals(current)) {
					next = graph.getEdgeSource(edge);
				} else {
					throw new Error("Should not happen!!!");
				}

				if (closed.contains(next)) {
					continue;
				}

				double tentativeGScore = gScores.get(current) + graph.getEdgeWeight(edge);

				if (!open.contains(next)) {
					hScores.put(next, h.getHeuristicEstimate(next, endVertex));

					cameFrom.put(next, edge);
					gScores.put(next, tentativeGScore);
					fScores.put(next, tentativeGScore + hScores.get(next));

					open.add(next);

				} else if (tentativeGScore < gScores.get(next)) {
					cameFrom.put(next, edge);
					gScores.put(next, tentativeGScore);
					fScores.put(next, tentativeGScore + hScores.get(next));
					// Required to sort the open list again
					open.remove(next);
					open.add(next);
				}
			}
		}
	}

	private List<E> reconstructEdgeList(V startVertex, V endVertex) {
		LinkedList<E> edgeList = new LinkedList<>();
		V current = endVertex;

		while (!current.equals(startVertex)) {
			E edge = cameFrom.get(current);
			edgeList.addFirst(edge);
			if (current.equals(graph.getEdgeTarget(edge))) {
				current = graph.getEdgeSource(edge);
			} else if (current.equals(graph.getEdgeSource(edge))) {
				current = graph.getEdgeTarget(edge);
			} else {
				throw new Error("!!!");
			}
		}

		return edgeList;
	}

	//~ Methods ----------------------------------------------------------------

	/**
	 * Return the edges making up the path found.
	 *
	 * @return List of Edges, or null if no path exists
	 */
	public List<E> getPathEdgeList() {
		if (path == null) {
			return null;
		} else {
			return path.getEdgeList();
		}
	}

	/**
	 * Return the path found.
	 *
	 * @return path representation, or null if no path exists
	 */
	public GraphPath<V, E> getPath() {
		return path;
	}

	/**
	 * Return the length of the path found.
	 *
	 * @return path length, or Double.POSITIVE_INFINITY if no path exists
	 */
	public double getPathLength() {
		if (path == null) {
			return Double.POSITIVE_INFINITY;
		} else {
			return getPath().getWeight();
		}
	}

	/**
	 * Convenience method to find the shortest path via a single static method call. If you need a more advanced search
	 * (e.g. limited by radius, or computation of the path length), use the constructor instead.
	 *
	 * @param graph
	 * 		the graph to be searched
	 * @param startVertex
	 * 		the vertex at which the path should start
	 * @param endVertex
	 * 		the vertex at which the path should end
	 *
	 * @return List of Edges, or null if no path exists
	 */
	public static <V, E> List<E> findPathBetween(Graph<V, E> graph, V startVertex, V endVertex,
												 Heuristic<V> heuristic) {
		AStarShortestPath<V, E> alg = new AStarShortestPath<>(graph, startVertex, endVertex, heuristic);

		return alg.getPathEdgeList();
	}

}
