package cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

public class PedestrianNetwork extends Network<SimulationNode, SimulationEdge> {

    public PedestrianNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }

}
