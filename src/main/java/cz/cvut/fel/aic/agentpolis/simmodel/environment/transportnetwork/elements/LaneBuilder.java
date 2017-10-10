package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;

import java.util.*;

/**
 * Lanes builder, controls uniqueness of lane´s id
 *
 * @author Zdenek Bousa
 */
@Singleton
public class LaneBuilder {
    private long laneCounter = 0;
    private Set<Lane> allLanes = new HashSet<>();

    /**
     * Create list of lanes for edge. List contains Lane, that contains information about available direction from the lane.
     * @param parent id of parent edge
     * @param map to be parsed
     * @return lanes available for edge + available direction from each lane, always returns List, at least with dummy
     * Lane[unknown,-1]
     */
    public List<Lane> createLanes(int parent, List<Map<String, Object>> map, int numberOfLanes) {
        List<Lane> lanes = new ArrayList<>();
        if (map == null) {
            // Add all directions
            for (int i = 0; i < numberOfLanes; i++){
                Lane lane = createNewLane(parent);
                lane.addDirection(-1,LaneTurnDirection.unknown);
                lanes.add(lane);
            }
            return lanes;
        }
        for (Map<String, Object> laneBuild : map) {
            Lane lane = createNewLane(parent);
            laneBuild.forEach((key, value) -> {

                // get direction
                LaneTurnDirection direction;
                if (LaneTurnDirection.contains(key)) direction = LaneTurnDirection.getEnumForKey(key);
                else direction = LaneTurnDirection.unknown;

                // get following edge
                int val = -1;
                if (value instanceof Integer) val = (int) value;
                else if (value instanceof Long) val = ((Long) value).intValue();

                // repair bug for id=(-1) and change this direction to "unknown"
                if (val == -1) direction = LaneTurnDirection.unknown;

                // add to list of lanes
                lane.addDirection(val, direction);
            });
            lanes.add(lane);
        }
        return lanes;
    }

    /**
     * Get builder to ensure proper lanes id
     * @return builder
     */
    public LaneBuilder getBuilder() {
        return this;
    }

    /**
     * Get all lanes added to the network
     * @return set of all lanes
     */
    public Set<Lane> getAllLanes() {
        return this.allLanes;
    }

    private Lane createNewLane(int parent) {
        Lane lane = new Lane(laneCounter++, parent);
        allLanes.add(lane);
        return lane;
    }
}