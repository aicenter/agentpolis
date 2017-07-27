package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

public class HighwayNetwork extends Network<SimulationNode, SimulationEdge> {

    public HighwayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }
}