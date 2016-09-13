package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.GraphBuilder;
import cz.agents.basestructures.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Marek Cuchy
 */
public class ComplexGraphBuilder {

    private final Map<Integer, SimulationNode> nodes = new HashMap<>();

    private final Table<Integer, Integer, SimulationEdge.SimulationEdgeBuilder> edges = HashBasedTable.create();

    public void clear() {
        nodes.clear();
        edges.clear();
    }

    /**
     * Build all graphs. Nodes without any edge are skipped.
     *
     * @return
     */
    public Map<GraphType, Graph<SimulationNode, SimulationEdge>> buildGraphByType() {
        Map<GraphType, GraphBuilder<SimulationNode, SimulationEdge>> builders = new HashMap<>();

        for (Cell<Integer, Integer, SimulationEdge.SimulationEdgeBuilder> cell : edges.cellSet()) {
            int fromNodeId = cell.getRowKey();
            int toNodeId = cell.getColumnKey();
            SimulationEdge.SimulationEdgeBuilder edgeBuilder = cell.getValue();

            SimulationEdge edge = createEdge(fromNodeId, toNodeId, edgeBuilder);

            for (GraphType type : edgeBuilder.graphTypes.keySet()) {
                GraphBuilder<SimulationNode, SimulationEdge> builder = getOrCreateBuilder(builders, type);
                addNode(fromNodeId, builder);
                addNode(toNodeId, builder);
                builder.addEdge(edge);
            }
        }

        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType = new HashMap<>();
        for (Entry<GraphType, GraphBuilder<SimulationNode, SimulationEdge>> entry : builders.entrySet()) {
            GraphType type = entry.getKey();
            Graph<SimulationNode, SimulationEdge> graph = entry.getValue().createGraph();
            graphByType.put(type, graph);
        }
        return graphByType;
    }

    private SimulationEdge createEdge(int fromNodeId, int toNodeId, SimulationEdge.SimulationEdgeBuilder edgeBuilder) {
        return edgeBuilder.build(fromNodeId, toNodeId);
    }

    private void addNode(int fromNodeId, GraphBuilder<SimulationNode, SimulationEdge> builder) {
        if (builder.getNode(fromNodeId) == null) {
            builder.addNode(nodes.get(fromNodeId));
        }
    }

    private GraphBuilder<SimulationNode, SimulationEdge> getOrCreateBuilder(
            Map<GraphType, GraphBuilder<SimulationNode, SimulationEdge>> builders, GraphType type) {
        GraphBuilder<SimulationNode, SimulationEdge> builder = builders.get(type);
        if (builder == null) {
            builder = GraphBuilder.createSubGraphBuilder();
            builders.put(type, builder);
        }
        return builder;
    }

    /**
     * Add a node with ID, location and description identical to {@code originalNode}.
     *
     * @param originalNode
     * @return {@code true} iff a node with the same ID already exists in the builder.
     */
    public boolean addNode(Node originalNode) {
        return addNode(originalNode.id, originalNode.sourceId, originalNode);
    }

    /**
     * Add new node with {@code nodeId}, {@code location} and {@code description}. It returns {@code true} iff a node
     * with the same {@code nodeId} already exists in the builder.
     *
     * @param nodeId
     * @param sourceId
     * @param location
     * @return {@code true} iff a node with the same {@code nodeId} already exists in the builder.
     */
    public boolean addNode(int nodeId, long sourceId, GPSLocation location) {
        return null != nodes.put(nodeId, new SimulationNode(nodeId, sourceId, location));
    }

    /**
     * Add complete edge with new {@code fromNodeId} and {@code toNodeId} or merge it with existing one.
     *
     * @param fromNodeId
     * @param toNodeId
     * @param edge
     */
    public void addEdge(int fromNodeId, int toNodeId, SimulationEdge edge) {
        SetMultimap graphTypes = edge.getGraphTypes();
        Map<GraphType, Integer> laneCounts = edge.getLaneCounts();

        SimulationEdge.SimulationEdgeBuilder edgeBuilder;
        if (edges.contains(fromNodeId, toNodeId)) {
            edgeBuilder = edges.get(fromNodeId, toNodeId);
        } else {
            edgeBuilder = new SimulationEdge.SimulationEdgeBuilder(edge);
            edges.put(fromNodeId, toNodeId, edgeBuilder);
        }

        for (Object o : graphTypes.values()) {
            Set<GraphType> sharedTypes = (Set<GraphType>) o;
            GraphType first = sharedTypes.iterator().next();
            edgeBuilder.addType(first);
            edgeBuilder.addLaneCount(first, laneCounts.get(first));
            for (GraphType newType : sharedTypes) {
                edgeBuilder.addSharedType(newType, first);
                edgeBuilder.addLaneCount(newType, laneCounts.get(newType));
            }
        }
    }

    /**
     * It adds completely new edge or merge it with existing one.
     *
     * @param edge
     */
    public void addEdge(SimulationEdge edge) {
        addEdge(edge.fromId, edge.toId, edge);
    }

    /**
     * Add new edge or add {@code laneCount} and {@code type} to an existing edge. The edge is considered as not shared
     * between original {@code GraphType}s and new {@code type}.
     *
     * @param fromNodeId
     * @param toNodeId
     * @param laneCount
     * @param length
     * @param type
     */
    public void addEdge(int fromNodeId, int toNodeId, int laneCount, int length, GraphType type) {
        if (edges.contains(fromNodeId, toNodeId)) {
            SimulationEdge.SimulationEdgeBuilder edgeBuilder = edges.get(fromNodeId, toNodeId);
            edgeBuilder.addLaneCount(type, laneCount);
            edgeBuilder.addType(type);
        } else {
            edges.put(fromNodeId, toNodeId, new SimulationEdge.SimulationEdgeBuilder(laneCount, length, type));
        }
    }

    /**
     * Add new {@code GraphType newType} and {@code laneCount} to an edge between {@code fromNodeId} and {@code
     * toNodeId}.
     *
     * @param fromNodeId
     * @param toNodeId
     * @param sharedType
     * @param newType
     * @param laneCount
     */
    public void addSharedEdge(int fromNodeId, int toNodeId, GraphType sharedType, GraphType newType, int laneCount) {
        if (!edges.contains(fromNodeId, toNodeId)) {
            throw new UnsupportedOperationException("The original shared edge has to be created before the new edge.");
        }

        SimulationEdge.SimulationEdgeBuilder edgeBuilder = edges.get(fromNodeId, toNodeId);
        if (!edgeBuilder.containsGraphType(sharedType))
            throw new IllegalArgumentException(" The edge has to have the shared type before.");
        edgeBuilder.addLaneCount(newType, laneCount);
        edgeBuilder.addSharedType(newType, sharedType);
    }

    /**
     * Checks if the edge between {@code fromNodeId} and {@code toNodeId} has at least one type from {@code types}.
     * Returns the contained {@code GraphType} or {@code null} if the edge doesn't contain any of {@code types}.
     *
     * @param fromNodeId
     * @param toNodeId
     * @param types
     * @return
     */
    public GraphType containsEdge(int fromNodeId, int toNodeId, GraphType... types) {
        SimulationEdge.SimulationEdgeBuilder edgeBuilder = edges.get(fromNodeId, toNodeId);
        if (edgeBuilder == null) return null;
        Set<GraphType> keySet = edgeBuilder.graphTypes.keySet();
        for (GraphType type : types) {
            if (keySet.contains(type)) return type;
        }
        return null;
    }

    public boolean containsEdge(int fromNodeId, int toNodeId) {
        return edges.contains(fromNodeId, toNodeId);
    }

    /**
     * Remove all graph {@code types} from builder.
     *
     * @param fromNodeId
     * @param toNodeId
     * @param types
     */
    public void removeEdge(int fromNodeId, int toNodeId, GraphType... types) {
        SimulationEdge.SimulationEdgeBuilder edgeBuilder = edges.get(fromNodeId, toNodeId);
        for (GraphType toRemove : types) {
            edgeBuilder.removeType(toRemove);
        }
    }

}
