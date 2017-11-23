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
package cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * Representation of vehicle
 *
 * @author Zbynek Moler
 */
public class PhysicalVehicle extends Vehicle {

    private final double lengthInMeters;
    private final int vehiclePassengerCapacity; // number of passenger, including driver
    private final EntityType vehicleType;
    private final GraphType usingGraphTypeForMoving;

    private final double maxVelocity;

    public PhysicalVehicle(String vehicleId, EntityType type, double lengthInMeters, int vehiclePassengerCapacity,
                           GraphType usingGraphTypeForMoving, SimulationNode position, double maxVelocity) {
        super(vehicleId, position);
        this.lengthInMeters = lengthInMeters;
        this.vehiclePassengerCapacity = vehiclePassengerCapacity;
        this.vehicleType = type;
        this.usingGraphTypeForMoving = usingGraphTypeForMoving;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public EntityType getType() {
        return vehicleType;
    }

    public int getCapacity() {
        return vehiclePassengerCapacity;
    }

    public double getLength() {
        return lengthInMeters;
    }

    public GraphType getGraphForMovingBaseOnType() {
        return usingGraphTypeForMoving;
    }

    @Override
    public double getVelocity() {
        return maxVelocity;
    }

}
