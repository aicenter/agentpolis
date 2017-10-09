package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;

import java.util.LinkedList;

/**
 * Lanes builder, controls uniqueness of lane´s id
 *
 * @author Zdenek Bousa
 */
@Singleton
public class LaneBuilder {
    private long laneCounter = 0;
    private LinkedList<Lane> allLanes = new LinkedList<>();

    public Lane createNewLane(int parent) {
        Lane lane = new Lane(laneCounter++, parent);
        allLanes.add(lane);
        return lane;
    }

    public LaneBuilder getBuilder() {
        return this;
    }

    public LinkedList<Lane> getAllLanes() {
        return this.allLanes;
    }
}