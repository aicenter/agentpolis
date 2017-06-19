package cz.agents.agentpolis.siminfrastructure.planner.path;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import cz.agents.agentpolis.siminfrastructure.planner.TripPlannerException;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The planner reuses DijkstraShortestPath from JGraphT
 *
 * @author Zbynek Moler
 */
public class ShortestPathPlanner {

    private final DirectedWeightedMultigraph<Integer, PlannerEdge> plannerGraph;
    private final TransportNetworks transportNetworks;

//    private Graph<SimulationNode, SimulationEdge> highwayGraph;


    @Inject
    public ShortestPathPlanner(TransportNetworks transportNetworks, @Assisted Set<GraphType> allowedGraphTypes) {
        super();

        DirectedWeightedMultigraph<Integer, PlannerEdge> plannerGraph
                = new DirectedWeightedMultigraph<>(PlannerEdge.class);
        Set<Integer> addedNodes = new HashSet<>();
        this.transportNetworks = transportNetworks;

        // temporary solution
//        highwayGraph = transportNetworks.getGraph(EGraphType.HIGHWAY);

        for (GraphType graphType : allowedGraphTypes) {
            Graph<SimulationNode, SimulationEdge> graph = transportNetworks.getGraph(graphType);
            for (Node node : graph.getAllNodes()) {
                int fromPositionByNodeId = node.id;
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

                    PlannerEdge plannerEdge = new PlannerEdge(graphType, fromPositionByNodeId, toPositionByNodeId);
                    plannerGraph.addEdge(fromPositionByNodeId, toPositionByNodeId, plannerEdge);
                    plannerGraph.setEdgeWeight(plannerEdge, edge.length);
                }

            }

        }
        this.plannerGraph = plannerGraph;
    }


    public VehicleTrip findTrip(String vehicleId, int startNodeById, int destinationNodeById) throws TripPlannerException {

        assert startNodeById != destinationNodeById : "Start finding position should not be the same as end finding "
                + "position";

        List<PlannerEdge> plannerEdges = findShortestPath(startNodeById, destinationNodeById);

        return createTrips(plannerEdges, vehicleId);

    }

    public Trip<Node> findTrip(int startNodeById, int destinationNodeById) throws TripPlannerException {

        assert startNodeById != destinationNodeById : "Start finding position should not be the same as end finding "
                + "position";

        if (startNodeById == destinationNodeById) {
            throw new TripPlannerException(startNodeById, destinationNodeById);
        }
        List<PlannerEdge> plannerEdges = findShortestPath(startNodeById, destinationNodeById);
        return createTrips(plannerEdges);
    }

    private List<PlannerEdge> findShortestPath(int fromPositionByNodeId,
                                               int toPositionByNodeId) throws TripPlannerException {
        DijkstraShortestPath<Integer, PlannerEdge> dijkstraShortestPath = new DijkstraShortestPath<>(plannerGraph,
                fromPositionByNodeId, toPositionByNodeId);
        List<PlannerEdge> plannerEdges = dijkstraShortestPath.getPathEdgeList();
        if (plannerEdges == null) {
            throw new TripPlannerException(fromPositionByNodeId, toPositionByNodeId);
        }

        return plannerEdges;

    }

    private VehicleTrip createTrips(List<PlannerEdge> plannerEdges, String vehicleId) {
        PlannerEdge plannerEdge = plannerEdges.get(0);
        GraphType graphType = plannerEdge.graphType;
        LinkedList<TripItem> path = new LinkedList<>();

        if (plannerEdges.size() > 0) {
            path.add(new TripItem(plannerEdge.fromPositionByNodeId));

            for (PlannerEdge plannerEdgeInner : plannerEdges) {

                if (graphType != plannerEdgeInner.graphType) {
//					trips.addTrip(new VehicleTrip(path, graphType, vehicleId));
//					path = new LinkedList<>();
//					graphType = plannerEdgeInner.graphType;
                    path.add(new TripItem(plannerEdgeInner.fromPositionByNodeId));
                }

                path.add(new TripItem(plannerEdgeInner.toPositionByNodeId));

            }

//			trips.addTrip(new VehicleTrip(path, graphType, vehicleId));

        }

        return new VehicleTrip(path, graphType, vehicleId);
    }

    private Trip<Node> createTrips(List<PlannerEdge> plannerEdges) {
        PlannerEdge plannerEdge = plannerEdges.get(0);
        GraphType graphType = plannerEdge.graphType;
        LinkedList<Node> path = new LinkedList<>();

        if (plannerEdges.size() > 0) {
            Graph<SimulationNode, SimulationEdge> graph = transportNetworks.getGraph(plannerEdge.graphType);
            path.add(graph.getNode(plannerEdge.fromPositionByNodeId));

            for (PlannerEdge plannerEdgeInner : plannerEdges) {
                if (graphType != plannerEdgeInner.graphType) {
                    graph = transportNetworks.getGraph(plannerEdge.graphType);
                    path.add(graph.getNode(plannerEdgeInner.fromPositionByNodeId));
                }
                graph = transportNetworks.getGraph(plannerEdge.graphType);
                path.add(graph.getNode(plannerEdgeInner.toPositionByNodeId));
            }
        }

        return new Trip(path);
    }

    public ShortestPathPlanner createShortestPathPlanner(Injector injector, Set<GraphType> allowedGraphTypes) {
//        DirectedWeightedMultigraph<Integer, PlannerEdge> plannerGraph = new DirectedWeightedMultigraph<>(PlannerEdge
//                .class);
//        Set<Integer> addedNodes = new HashSet<>();
//
//        for (GraphType graphType : allowedGraphTypes) {
//            Graph<SimulationNode, SimulationEdge> graph = injector.getInstance(TransportNetworks.class).getGraph
//                    (graphType);
//            for (Node node : graph.getAllNodes()) {
//                int fromPositionByNodeId = node.id;
//                if (!addedNodes.contains(fromPositionByNodeId)) {
//                    addedNodes.add(fromPositionByNodeId);
//                    plannerGraph.addVertex(fromPositionByNodeId);
//                }
//
//                for (Edge edge : graph.getOutEdges(node.id)) {
//                    Integer toPositionByNodeId = edge.toId;
//                    if (!addedNodes.contains(toPositionByNodeId)) {
//                        addedNodes.add(toPositionByNodeId);
//                        plannerGraph.addVertex(toPositionByNodeId);
//                    }
//
//                    PlannerEdge plannerEdge = new PlannerEdge(graphType, fromPositionByNodeId, toPositionByNodeId);
//                    plannerGraph.addEdge(fromPositionByNodeId, toPositionByNodeId, plannerEdge);
//                    plannerGraph.setEdgeWeight(plannerEdge, edge.length);
//                }
//
//            }
//
//        }

        return new ShortestPathPlanner(transportNetworks, allowedGraphTypes);
    }

    public static class PlannerEdge extends DefaultWeightedEdge {

        private static final long serialVersionUID = -7578001556242196908L;

        public final GraphType graphType;
        public final int fromPositionByNodeId;
        public final int toPositionByNodeId;

        public PlannerEdge(GraphType graphType, int fromPositionByNodeId, int toPositionByNodeId) {
            super();
            this.graphType = graphType;
            this.fromPositionByNodeId = fromPositionByNodeId;
            this.toPositionByNodeId = toPositionByNodeId;
        }

    }

    public interface ShortestPathPlannerFactory {
        public ShortestPathPlanner create(Set<GraphType> allowedGraphTypes);
    }

}
