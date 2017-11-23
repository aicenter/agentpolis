/* 
 * Copyright (C) 2017 fido.
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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public class VehicleTripData {
    private final PhysicalVehicle vehicle;
    
    private final Trip<SimulationNode> trip;
    
    
    private boolean tripFinished;

    public PhysicalVehicle getVehicle() {
        return vehicle;
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    public boolean isTripFinished() {
        return tripFinished;
    }

    public void setTripFinished(boolean tripFinished) {
        this.tripFinished = tripFinished;
    }
    
    

    
    public VehicleTripData(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        this.vehicle = vehicle;
        this.trip = trip;
        tripFinished = false;
    }
    
    
}
