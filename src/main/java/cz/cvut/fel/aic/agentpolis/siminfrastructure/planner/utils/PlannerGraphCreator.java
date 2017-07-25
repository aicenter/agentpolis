package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.utils;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DirectedWeightedMultigraph;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

public class PlannerGraphCreator {

	private PlannerGraphCreator() {
	}

	public static DirectedWeightedMultigraph<Integer, PlannerEdge> createGraph(
			Graph<SimulationNode, SimulationEdge> graph) {
		DirectedWeightedMultigraph<Integer, PlannerEdge> plannerGraph = new DirectedWeightedMultigraph<>(PlannerEdge
				.class);

		Set<Integer> addedNodes = new HashSet<>();

		for (Node node : graph.getAllNodes()) {
			Integer fromPositionByNodeId = node.id;
			if (!addedNodes.contains(fromPositionByNodeId)) {
				addedNodes.add(fromPositionByNodeId);
				plannerGraph.addVertex(fromPositionByNodeId);
			}

			for (Edge edge : graph.getOutEdges(node.id)) {
				Integer toPositionByNodeId = edge.toId;
				if (!addedNodes.contains(toPositionByNodeId)) {
					addedNodes.add(toPositionByNodeId);
					plannerGraph.addVertex(toPositionByNodeId);
				}

				PlannerEdge plannerEdge = new PlannerEdge(fromPositionByNodeId, toPositionByNodeId, edge.length);
				plannerGraph.addEdge(fromPositionByNodeId, toPositionByNodeId, plannerEdge);
				plannerGraph.setEdgeWeight(plannerEdge, edge.length);
			}

		}

		return plannerGraph;

	}

}
