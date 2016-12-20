package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.edge;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.gtdgraphimporter.structurebuilders.edge.EdgeBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Marek Cuchý
 * @author Zdenek Bousa
 */
public class RoadEdgeExtendedBuilder extends EdgeBuilder<RoadEdgeExtended> {

    private float allowedMaxSpeedInMpS;
    private long wayID;
    private Set<ModeOfTransport> modeOfTransports = EnumSet.noneOf(ModeOfTransport.class);

    private int oppositeWayId = 0;
    private int lanesCount = 1;

    public RoadEdgeExtendedBuilder() {
    }

    public RoadEdgeExtendedBuilder(int tmpFromId, int tmpToId) {
        super(tmpFromId, tmpToId);
    }

    public RoadEdgeExtendedBuilder(int tmpFromId, int tmpToId, int length) {
        super(tmpFromId, tmpToId, length);
    }

    public RoadEdgeExtendedBuilder(int tmpFromId, int tmpToId, int length, float allowedMaxSpeedInMpS, long wayID,
                                   Set<ModeOfTransport> modeOfTransports) {
        super(tmpFromId, tmpToId, length);
        this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;
        this.wayID = wayID;
        this.modeOfTransports = EnumSet.copyOf(modeOfTransports);
    }

    public float getAllowedMaxSpeedInMpS() {
        return allowedMaxSpeedInMpS;
    }

    public RoadEdgeExtendedBuilder setAllowedMaxSpeedInMpS(float allowedMaxSpeedInMpS) {
        this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;
        return this;
    }

    public long getWayID() {
        return wayID;
    }

    public RoadEdgeExtendedBuilder setWayID(long wayID) {
        this.wayID = wayID;
        return this;
    }

    public Set<ModeOfTransport> getModeOfTransports() {
        return modeOfTransports;
    }

    public RoadEdgeExtendedBuilder addModeOfTransports(Set<ModeOfTransport> ModeOfTransports) {
        this.modeOfTransports.addAll(ModeOfTransports);
        return this;
    }

    public RoadEdgeExtendedBuilder intersectModeOfTransports(Set<ModeOfTransport> ModeOfTransports) {
        this.modeOfTransports.retainAll(ModeOfTransports);
        return this;
    }

    public RoadEdgeExtendedBuilder setModeOfTransports(Set<ModeOfTransport> ModeOfTransports) {
        this.modeOfTransports = EnumSet.copyOf(ModeOfTransports);
        return this;
    }

    public boolean equalAttributes(RoadEdgeExtendedBuilder that) {
        return Float.compare(that.allowedMaxSpeedInMpS, allowedMaxSpeedInMpS) == 0 && wayID == that.wayID &&
                (modeOfTransports != null ? modeOfTransports.equals(that.modeOfTransports) :
                        that.modeOfTransports == null);
    }

    @Override
    public RoadEdgeExtended build(int fromId, int toId) {
        return new RoadEdgeExtended(fromId, toId, wayID, modeOfTransports, allowedMaxSpeedInMpS,
                getLength(), oppositeWayId, lanesCount);
    }

    @Override
    public boolean checkFeasibility(ModeOfTransport mode) {
        return modeOfTransports.contains(mode);
    }

    @Override
    public RoadEdgeExtendedBuilder copy(int tmpFromId, int tmpToId, int length) {
        return new RoadEdgeExtendedBuilder(tmpFromId, tmpToId, length, allowedMaxSpeedInMpS, wayID, modeOfTransports);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RoadEdgeExtendedBuilder that = (RoadEdgeExtendedBuilder) o;

        return Float.compare(that.allowedMaxSpeedInMpS, allowedMaxSpeedInMpS) == 0 && wayID == that.wayID &&
                (modeOfTransports != null ? modeOfTransports.equals(that.modeOfTransports) :
                        that.modeOfTransports == null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (allowedMaxSpeedInMpS != +0.0f ? Float.floatToIntBits(allowedMaxSpeedInMpS) : 0);
        result = 31 * result + (int) (wayID ^ (wayID >>> 32));
        result = 31 * result + (modeOfTransports != null ? modeOfTransports.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoadEdgeBuilder [" +
                "[" + super.toString() + "], " +
                "allowedMaxSpeedInMpS=" + allowedMaxSpeedInMpS +
                ", wayID=" + wayID +
                ", modeOfTransports=" + modeOfTransports +
                ']';
    }
}
