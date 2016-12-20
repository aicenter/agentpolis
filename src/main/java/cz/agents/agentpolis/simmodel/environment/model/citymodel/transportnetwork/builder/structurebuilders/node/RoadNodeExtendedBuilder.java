package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.node;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadNodeExtended;
import cz.agents.basestructures.GPSLocation;
import cz.agents.gtdgraphimporter.structurebuilders.node.NodeBuilder;

/**
 * Copied RoadNodeBuilder for AgentPolis
 *
 * @author Marek Cuchý
 * @author Zdenek Bousa
 */
public class RoadNodeExtendedBuilder extends NodeBuilder<RoadNodeExtended> {
    private boolean isParkAndRide;
    private boolean isBikeSharingStation;

    public RoadNodeExtendedBuilder(int tmpId, long sourceId, GPSLocation location) {
        super(tmpId, sourceId, location);
    }


    public boolean isParkAndRide() {
        return isParkAndRide;
    }

    public RoadNodeExtendedBuilder setParkAndRide(boolean parkAndRide) {
        isParkAndRide = parkAndRide;
        return this;
    }

    public boolean isBikeSharingStation() {
        return isBikeSharingStation;
    }

    public RoadNodeExtendedBuilder setBikeSharingStation(boolean bikeSharingStation) {
        isBikeSharingStation = bikeSharingStation;
        return this;
    }

    @Override
    public RoadNodeExtended buildNode(int id) {
        return new RoadNodeExtended(id, sourceId, location.latE6, location.lonE6, location.latProjected, location
                .lonProjected, location.elevation, isParkAndRide, isBikeSharingStation);
    }

    @Override
    public String toString() {
        return "RoadNodeExtendedBuilder [" +
                "[" + super.toString() + "], " +
                "isParkAndRide=" + isParkAndRide +
                ", isBikeSharingStation=" + isBikeSharingStation +
                ']';
    }
}
