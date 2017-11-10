package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.Edge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import java.util.List;
import java.util.LinkedList;
/**
 * Extended RoadEdge, contains road situation (lanes,two way pairs,...)
 * Also provides extended identification of the edge.
 *
 * @author Zdenek Bousa
 */
public class SimulationEdge extends Edge {
    /**
     * unique ID for each edge, also recognize directions
     */
    private final int uniqueId;

    /**
     * -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
     */
    private final int oppositeWayId; // unique edge id that is in opposite direction, otherwise -1 (one-way)

    /**
     * Available lanes
     */
    private final int lanesCount;

    /**
     * List of lanes with their properties
     * Lanes turn                   lanesTurn{left,through|through,right}
     * Lanes continues to edges:    lanesContinuesToEdge = {{1,2},{2,3}}
     * Lanes count                  lanesCount = 2
     */
    private List<Lane> lanesTurn;
    
    /**
     * maximal allowed speed in meters per second
     */
    public final float allowedMaxSpeedInMpS;

    public final EdgeShape shape;


    /**
     * @param fromId sourceId
     * @param toId destinationId
     * @param uniqueWayId unique id of edge across simulation
     * @param allowedMaxSpeedInMpS maximal allowed speed in meters per second
     * @param lengthInMetres       -
     * @param oppositeWayId        -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway).
     *                             Input should be correct, it is not validated!
     * @param lanesCount           total number of lanes for ModeOfTransport-car
     * @param edgeShape            instance of EdgeShape representing the shape of the road edge
     */
    public SimulationEdge(int fromId,
                          int toId,
                          int uniqueWayId,
                          int oppositeWayId,
                          int lengthInMetres,
                          float allowedMaxSpeedInMpS,
                          int lanesCount,
                          EdgeShape edgeShape,
                          List<Lane> lanesTurn) {
        super(fromId, toId, lengthInMetres);

        this.uniqueId = uniqueWayId;
        this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;

        // opposite way
        if (oppositeWayId >= -1) {
            this.oppositeWayId = oppositeWayId;
        } else {
            this.oppositeWayId = -2;
        }

        // lanes count - merging
        if(lanesTurn.size() > 0){ // data available from lanesTurn
            this.lanesCount = lanesTurn.size();
        }else{
            if (lanesCount >= 1) {
                this.lanesCount = lanesCount;
            } else {
                this.lanesCount = 1; //minimum
            }
        }

        this.shape = edgeShape;
        this.lanesTurn = lanesTurn;
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
     * List of available lanes for this edge (and in this direction from-to)
     * @return list of lanes
     */
    public List<Lane> getLanes(){
        return lanesTurn;
    }

    /**
     * Information about number of lanes for cars.
     *
     * @return total number of lanes (minimum is 1)
     */
    public int getLanesCount() {
        return lanesCount;
    }

    /**
     * Get lane by its unique ID.
     * @param id long id of the lane in this edge
     * @return Lane or null, if edge does not contain Lane with specified id.
     */
    public Lane getLaneById(long id){
        for(Lane l : lanesTurn){
            if(l.getLaneUniqueId() == id){
                return l;
            }
        }
        return null;
    }

    public String getLogInfo() {
        return fromId + "-" + toId;
    }
}
