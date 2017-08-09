package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;

/**
 * Represents key based on graph type and position (composed from two position)
 * @author Zbynek Moler
 *
 */
public class GraphTypeAndFromToNodeKey {

	public final GraphType graphType;
	public final long fromNodeId;
	public final long toNodeId;

	public GraphTypeAndFromToNodeKey(GraphType graphType, long fromNodeId, long toNodeId) {
		this.graphType = graphType;
		this.fromNodeId = fromNodeId;
		this.toNodeId = toNodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fromNodeId ^ (fromNodeId >>> 32));
		result = prime * result + ((graphType == null) ? 0 : graphType.hashCode());
		result = prime * result + (int) (toNodeId ^ (toNodeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		GraphTypeAndFromToNodeKey other = (GraphTypeAndFromToNodeKey) obj;

		if (graphType != other.graphType) {
			return false;
		}

		if (fromNodeId != other.fromNodeId) {
			return false;
		}

		if (toNodeId != other.toNodeId) {
			return false;
		}

		return true;
	}

}
