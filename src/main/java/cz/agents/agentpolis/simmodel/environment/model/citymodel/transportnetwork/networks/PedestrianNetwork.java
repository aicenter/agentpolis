package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

public class PedestrianNetwork extends Network<SimulationNode, SimulationEdge> {

    public PedestrianNetwork(Graph<SimulationNode, SimulationEdge> network) {
        super(network);
    }

}
