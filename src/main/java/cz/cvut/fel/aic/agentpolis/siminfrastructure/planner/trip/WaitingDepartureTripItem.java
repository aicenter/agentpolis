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

/**
 * 
 * The implementation of {@code DepartureTripItem} which is under waiting
 * constrains for a departure. Providing information about the departure time
 * 
 * @author Zbynek Moler
 * 
 */
public class WaitingDepartureTripItem extends DepartureTripItem {

    public final long departureTime;
    public final boolean overMidnight;

    public WaitingDepartureTripItem(int tripPositionByNodeId, long departureTime,
                                    boolean overMidnight) {
        super(tripPositionByNodeId);
        this.departureTime = departureTime;
        this.overMidnight = overMidnight;
    }

    @Override
    public void visitDepartureTripItem(DepartureTripItemVisitor departureTripItemVisitor) {
        departureTripItemVisitor.visitDepartureTripItem(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (departureTime ^ (departureTime >>> 32));
        result = prime * result + (overMidnight ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        WaitingDepartureTripItem other = (WaitingDepartureTripItem) obj;
        if (departureTime != other.departureTime) {
            return false;
        }
        return overMidnight == other.overMidnight;
    }

}
