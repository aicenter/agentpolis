package cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;


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
