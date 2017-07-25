package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.agents.multimodalstructures.additional.ModeOfTransport;
import cz.agents.multimodalstructures.edges.RoadEdge;

import java.util.List;
import java.util.Set;

/**
 * Extended RoadEdge, contains road situation (lanes,two way pairs,...)
 * Also provides extended identification of the edge.
 * @author Zdenek Bousa
 */
public class SimulationEdge extends RoadEdge {
    /**
     * unique ID for each edge, also recognize directions
     */
    private final int uniqueId;

    /**
     * -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
     */
    private final int oppositeWayId; // unique edge id that is in opposite direction, otherwise -1 (one-way)

    /**
     * TODO: lanes
     * Lanes turn                   lanesTurn{left,through|through,right}
     * Lanes continues to edges:    lanesContinuesToEdge = {{1,2},{2,3}}
     * Lanes count                  lanesCount = 2
     */
    private final int lanesCount;
    private List<Lane> lanesTurn; // not implemented // TODO: lanes turning

    /**
     *
     * @param fromId sourceId
     * @param toId destinationId
     * @param osmWayID osm id of this edge
     * @param uniqueWayId
     * @param permittedModes int representation of permited modes
     * @param allowedMaxSpeedInMpS maximal allowed speed in meters per second
     * @param lengthInMetres -
     * @param oppositeWayId -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway).
     *                      Input should be correct, it is not validated!
     * @param lanesCount total number of lanes for ModeOfTransport-car
     */
    public SimulationEdge(int fromId,
                            int toId,
                            long osmWayID,
                            int uniqueWayId,
                            int oppositeWayId,
                            int lengthInMetres,
                            Set<ModeOfTransport> permittedModes,
                            float allowedMaxSpeedInMpS,
                            int lanesCount) {
        super(fromId, toId, osmWayID, permittedModes, allowedMaxSpeedInMpS, lengthInMetres);

        this.uniqueId = uniqueWayId;

        if (oppositeWayId >= -1) {
            this.oppositeWayId = oppositeWayId;
        } else {
            this.oppositeWayId = -2;
        }

        if (lanesCount >= 1) {
            this.lanesCount = lanesCount;
        } else {
            this.lanesCount = 1; //minimum
        }

    }

    /**
     * ID
     *
     * @return unique id of the edge
     */
    public int getUniqueId() {
        return uniqueId;
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
    
    public String getLogInfo(){
        return fromId + "-" + toId; 
    }
}
