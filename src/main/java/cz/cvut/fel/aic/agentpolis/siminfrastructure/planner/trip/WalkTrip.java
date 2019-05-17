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

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;

/**
 * The implementation is a specific type of a general locations {@code GraphTrip}
 * representation. It represents a walk locations.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class WalkTrip extends GraphTrip<TripItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2416011174348898223L;

	public WalkTrip(LinkedList<TripItem> trip, GraphType graphType){
		super(trip, graphType);
	}

	@Override
	public void visit(TripVisitior tripVisitior) {
		tripVisitior.visitTrip(this);

	}

	@Override
	public WalkTrip clone() {
		LinkedList<TripItem> clonedTrip = new LinkedList<TripItem>();
		for (TripItem node : locations) {
			clonedTrip.addLast(new TripItem(node.tripPositionByNodeId));

		}
		return new WalkTrip(clonedTrip, graphType);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("WalkTrip: ");
		stringBuilder.append(super.toString());
		return stringBuilder.toString();
	}
}
