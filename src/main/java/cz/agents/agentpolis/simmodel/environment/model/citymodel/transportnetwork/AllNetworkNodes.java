package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 * 
 * The data wrapper for all nodes in networks and its provider
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class AllNetworkNodes {

    private Map<Integer, SimulationNode> allNetworkNodes;

	
	
	
	public Map<Integer, SimulationNode> getAllNetworkNodes() {
        return allNetworkNodes;
    }

	public void setAllNetworkNodes(Map<Integer, SimulationNode> allNetworkNodes) {
		this.allNetworkNodes = allNetworkNodes;
	}
	
	
	
	
	
	
	@Inject
	public AllNetworkNodes() {
	}

    public AllNetworkNodes(Map<Integer, SimulationNode> allNetworkNodes) {
        super();
        this.allNetworkNodes = allNetworkNodes;
    }
	

    

    public SimulationNode getNode(int nodeId) {
        return allNetworkNodes.get(nodeId);
    }

}
