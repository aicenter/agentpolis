package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

import java.util.Map;

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
