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
package cz.cvut.fel.aic.agentpolis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.AStarShortestPath;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.utils.PositionUtil;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;

/**
 *
 * @author david
 */

@Singleton
public class EuclideanTraveltimeHeuristic implements AStarAdmissibleHeuristic<SimulationNode>, 
		AStarShortestPath.Heuristic<SimulationNode>{
	
	private final PositionUtil positionUtil;

	@Inject
	public EuclideanTraveltimeHeuristic(PositionUtil positionUtil) {
		this.positionUtil = positionUtil;
	}
		
		

	@Override
	public double getCostEstimate(SimulationNode v, SimulationNode v1) {
		int distance = (int) Math.round(
			positionUtil.getPosition(v).distance(positionUtil.getPosition(v1)) * 100);
		
		return Math.round((double) distance / 3600 * 1000);
	}

	@Override
	public double getHeuristicEstimate(SimulationNode current, SimulationNode goal) {
		return getCostEstimate(current, goal);
	}

}
