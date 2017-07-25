package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.graph.IGraphGE;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.basestructures.Edge;

/**
 * The graph representation for GE visualization
 *
 * @author Zbynek Moler
 */
public class GraphGE implements IGraphGE {

	private final EntityPositionModel agentPositionStorage;
	private final EntityPositionModel vehiclePositionStorage;
	private final Collection<? extends Edge> edgesOfGraph;

	public GraphGE(EntityPositionModel agentPositionStorage, EntityPositionModel vehiclePositionStorage,
				   Collection<? extends Edge> edgesOfGraph) {
		super();
		this.agentPositionStorage = agentPositionStorage;
		this.vehiclePositionStorage = vehiclePositionStorage;
		this.edgesOfGraph = edgesOfGraph;
	}

	public Map<String, Long> getNumOfEntitiesPerEdge() {

		Map<Integer, Long> counterNumOfEntitiesPerNode = new HashMap<>();

		counterNumOfEntitiesPerNode = countNumOfEntitiesPerNode(counterNumOfEntitiesPerNode, agentPositionStorage);
		counterNumOfEntitiesPerNode = countNumOfEntitiesPerNode(counterNumOfEntitiesPerNode, vehiclePositionStorage);

		Map<String, Long> numOfEntitiesPerEdge = new HashMap<>();

		for (Edge edge : edgesOfGraph) {

			String edgeId = String.valueOf(edge.fromId + "-" + edge.toId);

			Long counterEntityPerEdge = numOfEntitiesPerEdge.get(edgeId);

			if (counterEntityPerEdge == null) {
				counterEntityPerEdge = 0L;
			}

			counterEntityPerEdge += getCountedEntitiesPerNode(edge.fromId, counterNumOfEntitiesPerNode);
			counterEntityPerEdge += getCountedEntitiesPerNode(edge.toId, counterNumOfEntitiesPerNode);

			numOfEntitiesPerEdge.put(edgeId, counterEntityPerEdge);

		}

		return numOfEntitiesPerEdge;
	}

	private long getCountedEntitiesPerNode(int nodeId, Map<Integer, Long> counterNumOfEntitiesPerNode) {
		Long counter = counterNumOfEntitiesPerNode.get(nodeId);
		if (counter == null) {
			return 0;
		}
		return counter;
	}

	public Map<Integer, Long> countNumOfEntitiesPerNode(Map<Integer, Long> counterNumOfEntitiesPerNode,
														EntityPositionModel entityPositionStorage) {
		Set<String> ids = new HashSet<>(entityPositionStorage.getIDs());

		for (String entityId : ids) {
			Integer entityPositionByNodeId = entityPositionStorage.getEntityPositionByNodeId(entityId);
			if (entityPositionByNodeId == null) {
				continue;
			}

			Long counter = counterNumOfEntitiesPerNode.get(entityPositionByNodeId);
			if (counter == null) {
				counter = 0L;
			}
			counter++;
			counterNumOfEntitiesPerNode.put(entityPositionByNodeId, counter);

		}
		return counterNumOfEntitiesPerNode;
	}

}
