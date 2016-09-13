package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

/**
 *
 *@author Marek Cuchy
 *
 */
public class BikewayNetwork extends Network<SimulationNode,SimulationEdge> {

    public BikewayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }
}
