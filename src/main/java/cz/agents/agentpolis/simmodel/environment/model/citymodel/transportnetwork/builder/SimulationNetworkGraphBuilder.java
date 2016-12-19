package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder;

import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.GraphBuilder;
import cz.agents.multimodalstructures.edges.RoadEdge;
import cz.agents.multimodalstructures.nodes.RoadNode;
import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zdenek Bousa
 */

public class SimulationNetworkGraphBuilder {
    private static final Logger LOGGER = Logger.getLogger(SimulationNetworkGraphBuilder.class);

    public SimulationNetworkGraphBuilder() {
    }

    /**
     * Method that will construct from RoadNode/RoadEdge and few extra tags SimulationNode/SimulationEdge
     *
     * @param highwayGraphFromOSM for retriewinf extra tags that are not in RoadEdge/RoadNode
     * @return
     */
    public Graph<SimulationNode, SimulationEdge> buildSimulationGraph(Graph<? extends RoadNode, ? extends RoadEdge> highwayGraphFromOSM) {

        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = GraphBuilder.createGraphBuilder();
        for (RoadNode roadNode : highwayGraphFromOSM.getAllNodes()) {
            SimulationNode simulationNode = new SimulationNode(roadNode);
            graphBuilder.addNode(simulationNode);
        }

        for (RoadEdge roadEdge : highwayGraphFromOSM.getAllEdges()) {
            SimulationEdge.SimulationEdgeBuilder edgeBuilder = new SimulationEdge.SimulationEdgeBuilder(roadEdge);
            SimulationEdge simulationEdge = edgeBuilder.build(roadEdge.fromId, roadEdge.toId);
            graphBuilder.addEdge(simulationEdge);
        }
        return graphBuilder.createGraph();
    }

    public Graph<SimulationNode, SimulationEdge> connectivity(Graph<SimulationNode, SimulationEdge> graph) {

        DirectedGraph<Integer, ShortestPathPlanner.PlannerEdge> plannerGraph = prepareGraphForFindComponents(graph);

        StrongConnectivityInspector<Integer, ShortestPathPlanner.PlannerEdge> strongConnectivityInspector = new StrongConnectivityInspector<>(
                plannerGraph);

        if (strongConnectivityInspector.isStronglyConnected()) {
            return graph;
        }

        LOGGER.debug("The Highway map has more then one strong component, it will be selected the largest components");

        Set<Integer> strongestComponents = getTheLargestGraphComponent(strongConnectivityInspector);

        return createGraphBasedOnTheLargestComponent(graph, strongestComponents);
    }

    private DirectedGraph<Integer, ShortestPathPlanner.PlannerEdge> prepareGraphForFindComponents(Graph<SimulationNode,
            SimulationEdge> graph) {

        DirectedGraph<Integer, ShortestPathPlanner.PlannerEdge> plannerGraph = new DefaultDirectedGraph<>(
                ShortestPathPlanner.PlannerEdge.class);
        Set<Integer> addedNodes = new HashSet<>();

        for (SimulationNode node : graph.getAllNodes()) {
            Integer fromPositionByNodeId = node.getId();
            if (!addedNodes.contains(fromPositionByNodeId)) {
                addedNodes.add(fromPositionByNodeId);
                plannerGraph.addVertex(fromPositionByNodeId);
            }

            for (SimulationEdge edge : graph.getOutEdges(node.getId())) {
                Integer toPositionByNodeId = edge.getToId();
                if (!addedNodes.contains(toPositionByNodeId)) {
                    addedNodes.add(toPositionByNodeId);
                    plannerGraph.addVertex(toPositionByNodeId);
                }

                ShortestPathPlanner.PlannerEdge plannerEdge = new ShortestPathPlanner.PlannerEdge(null, fromPositionByNodeId, toPositionByNodeId);
                plannerGraph.addEdge(fromPositionByNodeId, toPositionByNodeId, plannerEdge);
                // plannerGraph.setEdgeWeight(plannerEdge, edge.getLenght());
            }

        }
        return plannerGraph;
    }

    private Set<Integer> getTheLargestGraphComponent(
            StrongConnectivityInspector<Integer, ShortestPathPlanner.PlannerEdge> strongConnectivityInspector) {
        List<Set<Integer>> components = strongConnectivityInspector.stronglyConnectedSets();
        Collections.sort(components, (o1, o2) -> o2.size() - o1.size());
        return components.get(0);
    }

    private Graph<SimulationNode, SimulationEdge> createGraphBasedOnTheLargestComponent(Graph<SimulationNode, SimulationEdge> graph,
                                                                                        Set<Integer> strongestComponents) {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        for (Integer nodeId : strongestComponents) {
            graphBuilder.addNode(graph.getNode(nodeId));
        }

        for (Integer nodeId : strongestComponents) {
            for (SimulationEdge edge : graph.getOutEdges(nodeId)) {
                if (strongestComponents.contains(edge.getToId())) {
                    graphBuilder.addEdge(graph.getEdge(nodeId, edge.getToId()));
                }
            }
        }
        return graphBuilder.createGraph();
    }
}
