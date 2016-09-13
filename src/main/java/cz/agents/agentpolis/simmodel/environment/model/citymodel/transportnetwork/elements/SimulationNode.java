package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import cz.agents.basestructures.GPSLocation;
import cz.agents.multimodalstructures.nodes.RoadNode;

public class SimulationNode extends RoadNode {

    private static final long serialVersionUID = 6338634297573324955L;

    public SimulationNode(int id, long sourceId, GPSLocation loc, boolean isParkAndRide, boolean isBikeSharingStation) {
        super(id, sourceId, loc.latE6, loc.lonE6, loc.latProjected, loc.lonProjected, loc.elevation, isParkAndRide, isBikeSharingStation);
    }

    public SimulationNode(int id, long sourceId, GPSLocation loc) {
        this(id, sourceId, loc, false, false);
    }

    public SimulationNode(RoadNode node) {
        super(node.id, node.sourceId, node.latE6, node.lonE6, node.latProjected, node.lonProjected, node.elevation, node.isParkAndRide, node.isBikeSharingStation);
    }


}
