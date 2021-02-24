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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleTripData;

/**
 *
 * @author fido
 */
public class VehicleTransferData extends VehicleEventData{
	public final Lane from;

	public final Lane to;

	public final VehicleTripData vehicleTripData;

	public VehicleTransferData(Lane from, Lane to, VehicleTripData vehicleTripData, long transferFinishTime) {
		super(transferFinishTime);
		this.from = from;
		this.to = to;
		this.vehicleTripData = vehicleTripData;
	}
}
