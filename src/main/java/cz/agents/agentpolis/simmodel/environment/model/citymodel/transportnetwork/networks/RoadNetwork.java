package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.basestructures.Graph;

/**
 * Contains data for road network. Speed, lanes,...
 *
 * @author Zdenek Bousa
 */
public class RoadNetwork extends Network<RoadNodeExtended, RoadEdgeExtended> {

    public RoadNetwork(Graph<RoadNodeExtended, RoadEdgeExtended> network) {
        super(network);
    }
}
