/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import cz.cvut.fel.aic.geographtools.WKTPrintableCoord;

/**
 * The atomic representation of position in a trip
 *
 * @author Zbynek Moler
 */
public class TripItem implements WKTPrintableCoord{

	public final int tripPositionByNodeId;

	public TripItem(int tripPositionByNodeId) {

		this.tripPositionByNodeId = tripPositionByNodeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TripItem tripItem = (TripItem) o;

		return tripPositionByNodeId == tripItem.tripPositionByNodeId;

	}

	@Override
	public int hashCode() {
		return tripPositionByNodeId;
	}

	public String toString() {
		return Long.toString(tripPositionByNodeId);
	}

	@Override
	public String toWKTCoordinate() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
