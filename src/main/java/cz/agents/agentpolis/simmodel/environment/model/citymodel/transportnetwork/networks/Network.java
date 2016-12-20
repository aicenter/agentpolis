package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;



/**
 * 
 * 
 * The representation of a particular transport network
 * 
 * @author Zbynek Moler
 * 
 * @param <TNode>
 * @param <TEdge>
 */
public abstract class Network<TNode extends Node, TEdge extends Edge> {

    private final Graph<TNode, TEdge> network;

    public Network(Graph<TNode, TEdge> network) {
        super();
        this.network = network;
    }

    public Graph<TNode, TEdge> getNetwork() {
        return network;
    }
}
