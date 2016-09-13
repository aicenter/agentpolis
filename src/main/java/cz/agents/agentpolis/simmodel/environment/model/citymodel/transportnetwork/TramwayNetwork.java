package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

public class TramwayNetwork extends Network<SimulationNode, SimulationEdge> {

    public TramwayNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }
}
