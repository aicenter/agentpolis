package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

public class MetrowayNetwork extends Network<SimulationNode, SimulationEdge> {

    public MetrowayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }
}
