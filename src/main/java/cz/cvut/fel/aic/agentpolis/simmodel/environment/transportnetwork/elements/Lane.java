package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

/**
 * Basic element that contains information about its heading and which edge is parent.
 *
 * @author Zdenek Bousa
 * Note: Some people prefer to tag narrow two-way roads with lanes=1.5.
 */
public class Lane {
    private static final Logger LOGGER = Logger.getLogger(Lane.class);
    private final long laneUniqueId;
    private final int parentEdgeUniqueId;
    private LinkedList<Integer> directionWithID = new LinkedList<>();
    private LinkedList<LaneTurnDirection> directionEnumForID = new LinkedList<>();

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

    public void addDirection(int followingEdge, LaneTurnDirection directionEnum){
        directionWithID.add(followingEdge);
        directionEnumForID.add(directionEnum);
    }

    /**
     * Get ID of next edge in specified direction
     *
     * @param directionEnum - LaneTurnDirection
     * @return -1 if there is no connection for selected direction, else it returns id of following edge.
     */
    public int getEdgeIdForDirection(LaneTurnDirection directionEnum) {
        int index = directionEnumForID.indexOf(directionEnum);
        if (index == -1) {
            return -1;
        } else {
            return directionWithID.get(index);
        }
    }

    /**
     * Get enum name of direction by ID of following edge
     *
     * @param directionID =  edge id
     * @return enum laneTurnDirection
     */
    public LaneTurnDirection getDirectionEnumForID(int directionID) {
        int index = directionWithID.indexOf(directionID);
        if (index != -1) {
            return directionEnumForID.get(index);
        }
        return null;
    }

    /**
     * List of edges´ ID
     *
     * @return list of existing connections to different edges from end of this lane.
     */
    public LinkedList<Integer> getDirectionsID() {
        return directionWithID;
    }

    /**
     * List of directions
     *
     * @return list of existing directions from end of this lane.
     */
    public LinkedList<LaneTurnDirection> getDirectionsEnum() {
        return directionEnumForID;
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

    /**
     * Lanes builder, controls uniqueness of id
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
}
