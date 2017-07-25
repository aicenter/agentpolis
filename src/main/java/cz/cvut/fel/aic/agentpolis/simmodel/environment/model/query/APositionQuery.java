package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query;

import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

import java.util.Map;

/**
 * Position query provides information about position of some entity (e.g. agent).
 *
 * @author Zbynek Moler
 */
@Singleton
public abstract class APositionQuery {

	private final EntityPositionModel entityPositionStorage;
	private final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;

	public APositionQuery(EntityPositionModel entityPositionStorage, TransportNetworks transportNetworks) {
		super();
		this.entityPositionStorage = entityPositionStorage;
		this.graphByType = transportNetworks.getGraphsByType();
	}

	public Integer getCurrentPositionByNodeId(String entityId) {
		return entityPositionStorage.getEntityPositionByNodeId(entityId);
	}

	public Node getCurrentPositionBaseOnGraphType(String entityId, GraphType graphType) {
		Integer nodeId = getCurrentPositionByNodeId(entityId);
		// FIXME: In original AgentPolis GE is possible to invoke null
		if (nodeId == null) {
			return null;
		}

		return getCurrentPositionBaseOnGraphType(graphType, nodeId);

	}

	public Node getCurrentPositionBaseOnGraphType(GraphType graphType, int currentPositionByNodeId) {
		return graphByType.get(graphType).getNode(currentPositionByNodeId);
	}
}
