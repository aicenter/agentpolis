package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;

/**
 * 
 * Represents key based on graph type and position
 * 
 * @author Zbynek Moler
 *
 */
public class GraphTypeAndToNodeKey {
	
	public final GraphType graphType;
	public final long toNodeId;

	public GraphTypeAndToNodeKey(GraphType graphType, long toNodeId) {
		this.graphType = graphType;
		this.toNodeId = toNodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graphType == null) ? 0 : graphType.hashCode());
		result = prime * result + (int) (toNodeId ^ (toNodeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		GraphTypeAndToNodeKey other = (GraphTypeAndToNodeKey) obj;

		if (graphType != other.graphType) {
			return false;
		}

		if (toNodeId != other.toNodeId){
			return false;
		}
		
		return true;
	}

}
