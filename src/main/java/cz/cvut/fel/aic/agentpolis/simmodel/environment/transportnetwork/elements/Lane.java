package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Basic element that contains information about its heading and which edge is parent.
 *
 * @author Zdenek Bousa
 */
public class Lane implements Serializable {
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

    /**
     * @param followingEdge id of available edge
     * @param directionEnum name of the direction,{@link LaneTurnDirection}
     */
    public void addDirection(int followingEdge, LaneTurnDirection directionEnum) {
        directions.put(directionEnum, followingEdge);
        directionById.put(followingEdge, directionEnum);
    }

    /**
     * Get ID of next edge in specified direction
     *
     * @param directionEnum - LaneTurnDirection
     * @return -1 for unknown situation, else it returns id of following edge.
     */
    public int getEdgeIdForDirection(LaneTurnDirection directionEnum) {
        if (directions.isEmpty()) {
            return -1;
        } else {
            return directions.getOrDefault(directionEnum, -1);
        }
    }

    /**
     * Get turn direction for specified edge
     *
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
