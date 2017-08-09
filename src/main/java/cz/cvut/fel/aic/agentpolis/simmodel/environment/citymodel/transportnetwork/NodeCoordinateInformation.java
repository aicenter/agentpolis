package cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork;

import java.io.Serializable;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * The class wraps projected coordinates into spatial. The suppose that input
 * coordinates will be in system supported meter unit
 * 
 * @author Zbynek Moler
 * 
 */
public class NodeCoordinateInformation implements Serializable {

    private static final long serialVersionUID = -8889069232786102814L;
    
    // long = node id, coordinate = in cartesian
    private final ImmutableMap<Long, Coordinate> nodesInCartesian;

    public NodeCoordinateInformation(ImmutableMap<Long, Coordinate> nodesInCartesian) {
        super();
        this.nodesInCartesian = nodesInCartesian;
    }

    public Coordinate getNodeInCartesian(long nodeId) {
        return nodesInCartesian.get(nodeId);
    }

    public double getDistanceInMeters(long currentNodeId, long goalNodeId) {
        return nodesInCartesian.get(currentNodeId).distance(nodesInCartesian.get(goalNodeId));
    }

}
