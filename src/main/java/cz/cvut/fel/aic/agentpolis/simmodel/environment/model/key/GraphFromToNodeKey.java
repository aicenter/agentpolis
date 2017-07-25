package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.key;

import java.io.Serializable;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.Edge;

public class GraphFromToNodeKey implements Serializable {
    private static final long serialVersionUID = 8639928733733261692L;

    public final GraphType graphType;
    public final long fromNodeByNodeId;
    public final long toNodeByNodeId;

    public GraphFromToNodeKey(GraphType graphType, long fromNodeByNodeId, long toNodeByNodeId) {
        super();
        this.graphType = graphType;
        this.fromNodeByNodeId = fromNodeByNodeId;
        this.toNodeByNodeId = toNodeByNodeId;
    }

    public GraphFromToNodeKey(GraphType graphType, Edge edge) {
        this.graphType = graphType;
        this.fromNodeByNodeId = edge.fromId;
        this.toNodeByNodeId = edge.toId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (fromNodeByNodeId ^ (fromNodeByNodeId >>> 32));
        result = prime * result + ((graphType == null) ? 0 : graphType.hashCode());
        result = prime * result + (int) (toNodeByNodeId ^ (toNodeByNodeId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        GraphFromToNodeKey other = (GraphFromToNodeKey) obj;
        if (graphType.equals(other.graphType) == false) {
            return false;
        }
        if (fromNodeByNodeId != other.fromNodeByNodeId) {
            return false;
        }

        return toNodeByNodeId == other.toNodeByNodeId;
    }

}