package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Node;

/**
 * For RoadEdgeExtended. Allows full control for AgentPolis needs. Same as RoadNode
 *
 * @author Zdenek Bousa
 */
public class SimulationNode extends Node {
    
    public SimulationNode(int id, long osmId, int latE6, int lonE6, int projectedLat, int projectedLon, int elevation) {
        super(id, osmId, latE6, lonE6, projectedLat, projectedLon, elevation);
    }

    public SimulationNode(int id, long sourceId, GPSLocation location) {
        super(id, sourceId, location);
    }
}
