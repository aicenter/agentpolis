package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.agents.multimodalstructures.additional.ModeOfTransport;
import cz.agents.multimodalstructures.edges.RoadEdge;

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
    private int oppositeWayId = -2;

    /**
     * TODO: lanes
     * Lanes turn                   lanesTurn{left,through|through,right}
     * Lanes continues to edges:    lanesContinuesToEdge = {{1,2},{2,3}}
     * Lanes count                  lanesCount = 2
     */
    private String lanes;

    /**
     * @param fromId
     * @param toId
     * @param wayID
     * @param permittedModes
     * @param allowedMaxSpeedInMpS
     * @param lengthInMetres
     */
    public RoadEdgeExtended(int fromId, int toId, long wayID, Set<ModeOfTransport> permittedModes, float allowedMaxSpeedInMpS, int lengthInMetres) {
        super(fromId, toId, wayID, permittedModes, allowedMaxSpeedInMpS, lengthInMetres);
    }
}
