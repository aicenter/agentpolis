package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;

import javax.inject.Singleton;
import java.util.Map;

/**
 * The information provider relating with position
 *
 * @author Zbynek Moler
 */
@Singleton
public class PositionQuery {

	private final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;

	@Inject
	public PositionQuery(TransportNetworks transportNetworks) {
		super();
		this.graphByType = transportNetworks.getGraphsByType();
	}

	public int getLengthBetweenPositions(GraphType graphType, int fromNodeById, int toNodeById) {
		Graph<?, ?> graph = graphByType.get(graphType);
		Edge edge = graph.getEdge(fromNodeById, toNodeById);
		return edge.length;

	}

}
