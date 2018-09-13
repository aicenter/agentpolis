package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import java.util.List;

public class TripUpdateMessage {

    private String id;
    private List<Long> nodeIds;

    public TripUpdateMessage(String id, List<Long> nodeIds) {
        this.id = id;
        this.nodeIds = nodeIds;
    }

    public String getId() {
        return id;
    }

    public List<Long> getNodeIds() {
        return nodeIds;
    }
}
