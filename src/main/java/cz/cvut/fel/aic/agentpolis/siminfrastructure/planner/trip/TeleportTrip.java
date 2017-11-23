/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
