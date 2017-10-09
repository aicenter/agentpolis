package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Basic element that contains information about its heading and which edge is parent.
 *
 * @author Zdenek Bousa
 * Note: Some people prefer to tag narrow two-way roads with lanes=1.5.
 */
public class Lane {
    private final long laneUniqueId;
    private final int parentEdgeUniqueId;
    private final HashMap<LaneTurnDirection, Integer> directions = new HashMap<>();
    private final HashMap<Integer, LaneTurnDirection> directionById = new HashMap<>();

    /**
     * Constructor
     *
     * @param laneUniqueId       - generated unique id, not evaluated.
     * @param parentEdgeUniqueId - id of parent edge
     */
    public Lane(long laneUniqueId, Integer parentEdgeUniqueId) {
        this.laneUniqueId = laneUniqueId;
        this.parentEdgeUniqueId = parentEdgeUniqueId;
    }

    public void addDirection(int followingEdge, LaneTurnDirection directionEnum) {
        directions.put(directionEnum, followingEdge);
        directionById.put(followingEdge, directionEnum);
    }

    /**
     * Get ID of next edge in specified direction
     *
     * @param directionEnum - LaneTurnDirection
     * @return -1 for unknown situation, -2 if there is no record for this direction, else it returns id of following edge.
     */
    public int getEdgeIdForDirection(LaneTurnDirection directionEnum) {
        if (directions.isEmpty()) {
            return -1;
        } else {
            return directions.getOrDefault(directionEnum, -2);
        }
    }

    /**
     * Get turn direction for specified edge
     * @param nextEdge int id
     * @return {@link LaneTurnDirection}
     */
    public LaneTurnDirection getDirectionForID(int nextEdge) {
        if (directionById.isEmpty() || !directionById.containsKey(nextEdge)) {
            return LaneTurnDirection.unknown;
        } else {
            return directionById.get(nextEdge);
        }
    }

    /**
     * @return available directions, enum {@link LaneTurnDirection}
     */
    public Set<LaneTurnDirection> getAvailableDirections() {
        return directions.keySet();
    }

    /**
     * @return unique ID of each lane
     */
    public long getLaneUniqueId() {
        return laneUniqueId;
    }

    /**
     * @return parent´s ID
     */
    public int getParentEdgeUniqueId() {
        return parentEdgeUniqueId;
    }
}
