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


import static com.google.common.base.Preconditions.checkNotNull;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.geographtools.WKTPrintableCoord;

/**
 *  
 * @author David Fiedler
 * @param <L>
 * 
 */
public class VehicleTrip<L extends WKTPrintableCoord> extends Trip<L> {

	private final Vehicle vehicle;

	public VehicleTrip(int tripId,Vehicle vehicle, L... locations){
		super(tripId,locations);
		this.vehicle = checkNotNull(vehicle);
	}

	public Vehicle getVehicle() {
		return vehicle;
	}
}
