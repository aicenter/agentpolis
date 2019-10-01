/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
	 * @param velocityInCmPerSecond Velocity in centimeters per second.
	 * @param lengthInCm Distance in centimeters per second.
	 * @return Required time in milliseconds.
	 */
	public static long computeDuration(int velocityInCmPerSecond, int lengthInCm) {

		// Compute duration on edge (in miliseconds)
		long duration = Math.round((double) lengthInCm / velocityInCmPerSecond * 1000);

		// Minimal duration is 1 millisecond
		if (duration < 1) {
			duration = 1;
		}

		return duration;

	}

	public static int computeAgentOnEdgeVelocity(MovingEntity entity, SimulationEdge edge) {
		int postedSpeedCmPerSecond = edge.getAllowedMaxSpeedInCmPerSecond();
		int driverMaximalVelocityCmPerSecond = entity.getVelocity() * 100;
		return Integer.min(driverMaximalVelocityCmPerSecond, postedSpeedCmPerSecond);
	}
	
	public static long computeDuration(MovingEntity entity, SimulationEdge edge){
		int distance = edge.getLengthCm();
		int velocity = computeAgentOnEdgeVelocity(entity, edge);
		return computeDuration(velocity, distance);
	}
	
	public static long computeMinDuration(SimulationEdge edge){
		int distanceCm = edge.getLengthCm();
		int velocityCmPerSecond = edge.getAllowedMaxSpeedInCmPerSecond();
		return computeDuration(velocityCmPerSecond, distanceCm);
	}
}
