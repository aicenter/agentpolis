package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import java.util.Map;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 * 
 * The data wrapper for all nodes in networks and its provider
 * 
 * @author Zbynek Moler
 * 
 */
public class AllNetworkNodes {

    private final Map<Integer, SimulationNode> allNetworkNodes;

    public AllNetworkNodes(Map<Integer, SimulationNode> allNetworkNodes) {
        super();
        this.allNetworkNodes = allNetworkNodes;
    }

    public Map<Integer, SimulationNode> getAllNetworkNodes() {
        return allNetworkNodes;
    }

    public SimulationNode getNode(int nodeId) {
        return allNetworkNodes.get(nodeId);
    }

}
