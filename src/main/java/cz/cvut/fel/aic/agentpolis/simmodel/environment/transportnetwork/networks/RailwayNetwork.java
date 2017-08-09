package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

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
