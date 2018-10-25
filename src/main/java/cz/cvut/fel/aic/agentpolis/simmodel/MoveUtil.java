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
package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;


/**
 * Utility for support movement. - It computes duration
 *
 * @author Zbynek Moler
 */
public final class MoveUtil {

    private MoveUtil() {
    }

    /**
     * Returns the time required for traveling for the given distance
     * with specified velocity.
     *
     * @param velocityInmps Velocity in metres per second.
     * @param lengthInMeter Distance in metres per second.
     * @return Required time in milliseconds.
     * @note (FIXME) The result is in different units (milliseconds) than inputs
     * (metres per second and metres); this method should be deprecated.
     */
    public static long computeDuration(double velocityInmps, double lengthInMeter) {

        // Compute duration on edge (in miliseconds)
        long duration = Math.round(lengthInMeter / velocityInmps * 1000);

        // Minimal duration
        if (duration < 1) {
            duration = 1;
        }

        return duration;

    }

    public static double computeAgentOnEdgeVelocity(double driverMaximalVelocity, float allowedMaxSpeedOnRoad) {
        return Double.min(driverMaximalVelocity, allowedMaxSpeedOnRoad);
    }
	
	public static long computeDuration(MovingEntity entity, SimulationEdge edge){
		double distance = edge.shape.getShapeLength();
		double velocity = computeAgentOnEdgeVelocity(entity.getVelocity(), edge.allowedMaxSpeedInMpS);
		return computeDuration(velocity, distance);
	}
	
	public static long computeMinDuration(SimulationEdge edge){
		double distance = edge.shape.getShapeLength();
		return computeDuration(edge.allowedMaxSpeedInMpS, distance);
	}
}
