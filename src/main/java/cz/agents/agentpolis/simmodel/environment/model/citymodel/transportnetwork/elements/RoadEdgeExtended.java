package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.agents.multimodalstructures.additional.ModeOfTransport;
import cz.agents.multimodalstructures.edges.RoadEdge;

import java.util.List;
import java.util.Set;

/**
 * Extended RoadEdge, contains road situation (lanes,two way pairs,...)
 *
 * @author Zdenek Bousa
 */
public class RoadEdgeExtended extends RoadEdge {
    /**
     * -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
     */
    private final int oppositeWayId;

    /**
     * TODO: lanes
     * Lanes turn                   lanesTurn{left,through|through,right}
     * Lanes continues to edges:    lanesContinuesToEdge = {{1,2},{2,3}}
     * Lanes count                  lanesCount = 2
     */
    private final int lanesCount;
    private List<Lane> lanes;

    /**
     *
     * @param fromId sourceId
     * @param toId destinationId
     * @param osmWayID osm id of this edge
     * @param permittedModes int representation of permited modes
     * @param allowedMaxSpeedInMpS maximal allowed speed in meters per second
     * @param lengthInMetres -
     * @param oppositeWayId -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway).
     *                      Input should be correct, it is not validated!
     * @param lanesCount total number of lanes for ModeOfTransport-car
     */
    public RoadEdgeExtended(int fromId, int toId, long osmWayID, Set<ModeOfTransport> permittedModes,
                            float allowedMaxSpeedInMpS, int lengthInMetres, int oppositeWayId, int lanesCount) {
        super(fromId, toId, osmWayID, permittedModes, allowedMaxSpeedInMpS, lengthInMetres);

        if (lanesCount >= 1) {
            this.lanesCount = lanesCount;
        } else {
            this.lanesCount = 1; //minimum
        }

        if (oppositeWayId >= -1) {
            this.oppositeWayId = oppositeWayId;
        } else {
            this.oppositeWayId = -2;
        }

    }

    /**
     * Information about opposite direction on the road.
     *
     * @return -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
     */
    public int getOppositeWayId() {
        return oppositeWayId;
    }

    /**
     * Information about number of lanes for cars.
     *
     * @return total number of lanes (minimum is 1)
     */
    public int getLanesCount() {
        return lanesCount;
    }
}
