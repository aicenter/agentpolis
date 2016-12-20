package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

/**
 *
 *@author Marek Cuchy
 *
 */
public class RailwayNetwork extends Network<SimulationNode, SimulationEdge>{

    public RailwayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }

}
