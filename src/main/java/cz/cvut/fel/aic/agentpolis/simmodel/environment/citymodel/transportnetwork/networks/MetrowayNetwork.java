package cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

public class MetrowayNetwork extends Network<SimulationNode, SimulationEdge> {

    public MetrowayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }
}
