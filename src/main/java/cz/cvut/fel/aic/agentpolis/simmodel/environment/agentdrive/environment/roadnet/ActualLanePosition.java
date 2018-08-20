package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

/**
 * Created by david on 9/17/15.
 */
public class ActualLanePosition {
    private Lane lane;
    private Edge edge;
    private int index;

    public ActualLanePosition(Lane lane, int index) {
        this.lane = lane;
        this.index = index;
        edge = lane.getParentEdge();
    }

    public Lane getLane() {
        return lane;
    }

    public int getIndex() {
        return index;
    }

    public Edge getEdge() {
        return edge;
    }
}

