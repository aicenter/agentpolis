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
	public static long computeDuration(double velocityInCmPerSecond, int lengthInCm) {

		// Compute duration on edge (in miliseconds)
		long duration = Math.round((double) lengthInCm / velocityInCmPerSecond * 1000);

		// Minimal duration is 1 millisecond
		if (duration < 1) {
			duration = 1;
		}

		return duration;

	}

	public static double computeAgentOnEdgeVelocity(MovingEntity entity, SimulationEdge edge) {
		double postedSpeedCmPerSecond = edge.getAllowedMaxSpeedInCmPerSecond();
		double driverMaximalVelocityCmPerSecond = entity.getVelocity() * 100;
		return Double.min(driverMaximalVelocityCmPerSecond, postedSpeedCmPerSecond);
	}
	
	public static long computeDuration(MovingEntity entity, SimulationEdge edge){
		int distance = edge.getLengthCm();
		double velocity = computeAgentOnEdgeVelocity(entity, edge);
		return computeDuration(velocity, distance);
	}
	
	public static long computeMinDuration(SimulationEdge edge){
		int distanceCm = edge.getLengthCm();
		double velocityCmPerSecond = edge.getAllowedMaxSpeedInCmPerSecond();
		return computeDuration(velocityCmPerSecond, distanceCm);
	}
}
