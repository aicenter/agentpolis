package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

/**
 * Represents teleport trip, where trip contains start position, destination
 * position and distance.
 * 
 * @author Zbynek Moler
 * 
 */
public class TeleportTrip extends GraphTrip<TripItem> {

    /**
     * 
     */
    private static final long serialVersionUID = -7010112837573504352L;

    public final TripItem teleportFrom;
    public final TripItem teleportTo;
    public final double distBetweenFromTo;

    public TeleportTrip(TripItem teleportFrom, TripItem teleportTo, double distBetweenFromTo){
        super(definePath(teleportFrom, teleportTo), null);

        this.teleportFrom = teleportFrom;
        this.teleportTo = teleportTo;
        this.distBetweenFromTo = distBetweenFromTo;

    }

    private static LinkedList<TripItem> definePath(TripItem teleportFrom, TripItem teleportTo) {
        LinkedList<TripItem> path = new LinkedList<TripItem>();
        path.add(teleportFrom);
        path.add(teleportTo);
        return path;
    }

    @Override
    public void visit(TripVisitior tripVisitior) {
        tripVisitior.visitTrip(this);
    }

    @Override
    public TeleportTrip clone() {
		return new TeleportTrip(teleportFrom, teleportTo, distBetweenFromTo);
	}

    @Override
    public String toString() {
        return "TeleportTrip: from:" + teleportFrom.tripPositionByNodeId + ",to:"
                + teleportTo.tripPositionByNodeId + ",dist:" + distBetweenFromTo;
    }
}
