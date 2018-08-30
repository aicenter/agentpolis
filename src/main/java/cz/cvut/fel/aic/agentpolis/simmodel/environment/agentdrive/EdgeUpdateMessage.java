package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.ActualLanePosition;

public class EdgeUpdateMessage {
    private Integer carId;
    private long agentpolisNodeId;

    public EdgeUpdateMessage(Integer carId, long reachedAgentpolisNodeId) {
        this.carId = carId;
        this.agentpolisNodeId = reachedAgentpolisNodeId;
    }

    public Integer getCarId() {
        return carId;
    }

    public long getReachedAgentpolisNodeId() {
        return agentpolisNodeId;
    }
}
