package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
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