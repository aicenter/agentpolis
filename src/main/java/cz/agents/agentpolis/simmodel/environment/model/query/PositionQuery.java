package cz.agents.agentpolis.simmodel.environment.model.query;

import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;

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
