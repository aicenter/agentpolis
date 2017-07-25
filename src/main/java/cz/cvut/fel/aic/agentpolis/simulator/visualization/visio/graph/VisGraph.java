package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.graph;

import java.util.Collection;
import java.util.Map;

import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

/**
 * 
 * Graph wrapper for visio
 * 
 * @author Zbynek Moler
 *
 */
public class VisGraph {

    private final Map<Integer,Node> nodesByIds;
    private final Collection<Node>  nodes;
    private final Collection<Edge> edges;

    public VisGraph(Map<Integer, Node> nodesByIds, Collection<Node> nodes, Collection<Edge> edges) {
        super();
        this.nodesByIds = nodesByIds;
        this.nodes = nodes;
        this.edges = edges;
    }

    public Collection<Edge> getEdges() {    
        return edges;
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public Node getNode(int nodeId){
        return nodesByIds.get(nodeId);
    }
}
