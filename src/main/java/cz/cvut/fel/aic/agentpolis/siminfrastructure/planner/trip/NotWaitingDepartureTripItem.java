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
 * The implementation of {@code DepartureTripItem} which is not under waiting
 * constrains for a departure.
 * 
 * @author Zbynek Moler
 * 
 */
public class NotWaitingDepartureTripItem extends DepartureTripItem {

    public NotWaitingDepartureTripItem(int tripPositionByNodeId) {
        super(tripPositionByNodeId);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void visitDepartureTripItem(DepartureTripItemVisitor departureTripItemVisitor) {
        departureTripItemVisitor.visitDepartureTripItem(this);

    }

}
