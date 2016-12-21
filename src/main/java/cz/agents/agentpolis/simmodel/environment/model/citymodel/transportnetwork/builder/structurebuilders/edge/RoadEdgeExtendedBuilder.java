package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.builder.structurebuilders.edge;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.RoadEdgeExtended;
import cz.agents.gtdgraphimporter.structurebuilders.edge.EdgeBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;

import java.util.EnumSet;
import java.util.Set;

/**
 * RoadEdgeExtendedBuilder is used for temporary store of edge. Use build() to create RoadEdgeExtended
 * @author Zdenek Bousa
 */
public class RoadEdgeExtendedBuilder extends EdgeBuilder<RoadEdgeExtended> {

    private float allowedMaxSpeedInMpS;
    private long wayID; // OsmWay ID
    private int uniqueWayID;
    private int oppositeWayUniqueId; // -1 if does not exists,otherwise uniqueWayId of the direction edge
    private Set<ModeOfTransport> modeOfTransports = EnumSet.noneOf(ModeOfTransport.class);

    private int lanesCount;

    public RoadEdgeExtendedBuilder(int tmpFromId,
                                   int tmpToId,
                                   long OsmWayId,
                                   int uniqueWayId,
                                   int oppositeWayUniqueId,
                                   int length,
                                   Set<ModeOfTransport> modeOfTransports,
                                   float allowedMaxSpeedInMpS,
                                   int lanesCount) {
        super(tmpFromId, tmpToId, length);
        this.wayID = OsmWayId;
        this.modeOfTransports = EnumSet.copyOf(modeOfTransports);
        this.uniqueWayID = uniqueWayId;
        this.oppositeWayUniqueId = oppositeWayUniqueId;

        //extras
        this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;
        this.lanesCount = lanesCount;
    }

    @Override
    public RoadEdgeExtendedBuilder copy(int tmpFromId, int tmpToId, int length) {
        return new RoadEdgeExtendedBuilder(tmpFromId, tmpToId, wayID, uniqueWayID, oppositeWayUniqueId,
                length, modeOfTransports, allowedMaxSpeedInMpS, lanesCount);
    }

    /**
     * Build of new edge
     */
    @Override
    public RoadEdgeExtended build(int fromId, int toId) {
        return new RoadEdgeExtended(fromId, toId, wayID, uniqueWayID, oppositeWayUniqueId, getLength(), modeOfTransports,
                allowedMaxSpeedInMpS, lanesCount);
    }

    /**
     * Way ID
     */
    public long getWayID() {
        return wayID;
    }

    public RoadEdgeExtendedBuilder setWayID(long wayID) {
        this.wayID = wayID;
        return this;
    }

    public int getUniqueWayID() {
        return uniqueWayID;
    }

    public RoadEdgeExtendedBuilder setUniqueWayID(int uniqueWayID) {
        this.uniqueWayID = uniqueWayID;
        return this;
    }

    /**
     * Get the opposite direction edge
     *
     * @return -1 if does not exists,otherwise uniqueWayId of the direction edge
     */
    public int getOppositeWayUniqueId() {
        return oppositeWayUniqueId;
    }

    /**
     * Set the opposite direction edge.
     * -1 if does not exists,otherwise uniqueWayId of the direction edge.
     */
    public RoadEdgeExtendedBuilder setOppositeWayUniqueId(int oppositeWayUniqueId) {
        this.oppositeWayUniqueId = oppositeWayUniqueId;
        return this;
    }

    /**
     * Transport mods
     */
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
        return wayID == that.wayID
                && uniqueWayID == that.uniqueWayID
                && oppositeWayUniqueId == that.oppositeWayUniqueId
                && lanesCount == that.lanesCount
                && (modeOfTransports != null ? modeOfTransports.equals(that.modeOfTransports)
                && Float.compare(that.allowedMaxSpeedInMpS, allowedMaxSpeedInMpS) == 0 :
                that.modeOfTransports == null);
    }

    /**
     * Max Speed
     */
    public float getAllowedMaxSpeedInMpS() {
        return allowedMaxSpeedInMpS;
    }

    public RoadEdgeExtendedBuilder setAllowedMaxSpeedInMpS(float allowedMaxSpeedInMpS) {
        this.allowedMaxSpeedInMpS = allowedMaxSpeedInMpS;
        return this;
    }

    public int getLanesCount() {
        return lanesCount;
    }

    public RoadEdgeExtendedBuilder setLanesCount(int lanesCount) {
        this.lanesCount = lanesCount;
        return this;
    }

    /**
     * Implemented from EdgeBuilder
     */
    @Override
    public boolean checkFeasibility(ModeOfTransport mode) {
        return modeOfTransports.contains(mode);
    }

    /**
     * Generated
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RoadEdgeExtendedBuilder that = (RoadEdgeExtendedBuilder) o;

        return Float.compare(that.allowedMaxSpeedInMpS, allowedMaxSpeedInMpS) == 0
                && wayID == that.wayID
                && uniqueWayID == that.uniqueWayID
                && oppositeWayUniqueId == that.oppositeWayUniqueId
                && lanesCount == that.lanesCount
                && (modeOfTransports != null ? modeOfTransports.equals(that.modeOfTransports) : that.modeOfTransports == null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (allowedMaxSpeedInMpS != +0.0f ? Float.floatToIntBits(allowedMaxSpeedInMpS) : 0);
        result = 31 * result + (int) (wayID ^ (wayID >>> 32));
        result = 31 * result + uniqueWayID;
        result = 31 * result + oppositeWayUniqueId;
        result = 31 * result + (modeOfTransports != null ? modeOfTransports.hashCode() : 0);
        result = 31 * result + lanesCount;
        return result;
    }

    @Override
    public String toString() {
        return "RoadEdgeExtendedBuilder{" + super.toString() +
                ", allowedMaxSpeedInMpS=" + allowedMaxSpeedInMpS +
                ", wayID=" + wayID +
                ", uniqueWayID=" + uniqueWayID +
                ", oppositeWayUniqueId=" + oppositeWayUniqueId +
                ", modeOfTransports=" + modeOfTransports +
                ", lanesCount=" + lanesCount +
                '}';
    }
}
