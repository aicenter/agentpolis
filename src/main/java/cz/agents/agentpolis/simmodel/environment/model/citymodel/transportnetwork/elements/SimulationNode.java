package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.agents.basestructures.GPSLocation;
import cz.agents.multimodalstructures.nodes.RoadNode;

/**
 * For RoadEdgeExtended. Allows full control for AgentPolis needs. Same as RoadNode
 *
 * @author Zdenek Bousa
 */
public class SimulationNode extends RoadNode {
    
    public SimulationNode(int id, long osmId, int latE6, int lonE6, int projectedLat, int projectedLon, int elevation, boolean isParkAndRide, boolean isBikeSharingStation) {
        super(id, osmId, latE6, lonE6, projectedLat, projectedLon, elevation, isParkAndRide, isBikeSharingStation);
    }

    public SimulationNode(int id, long sourceId, GPSLocation location, boolean isParkAndRide, boolean isBikeSharingStation) {
        super(id, sourceId, location, isParkAndRide, isBikeSharingStation);
    }
}
