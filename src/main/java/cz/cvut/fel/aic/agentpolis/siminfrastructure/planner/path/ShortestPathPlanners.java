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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripPlannerException;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner.ShortestPathPlannerFactory;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author fido
 */
@Singleton
public class ShortestPathPlanners {
	private final HashMap<Set<GraphType>, ShortestPathPlanner> shortestPathPlannersMappedByGraphTypes;
	
	private final ShortestPathPlannerFactory shortestPathPlannerFactory;

	@Inject
	public ShortestPathPlanners(ShortestPathPlannerFactory shortestPathPlannerFactory) {
		this.shortestPathPlannerFactory = shortestPathPlannerFactory;
		this.shortestPathPlannersMappedByGraphTypes = new HashMap<>();
	}
	
	public VehicleTrip findTrip(String vehicleId, int startNodeById, int destinationNodeById, Set<GraphType> graphTypes) 
			throws TripPlannerException{
		if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
			createShortestPathPlanner(graphTypes);
		}
		
		ShortestPathPlanner planner = shortestPathPlannersMappedByGraphTypes.get(graphTypes);
		
		return planner.findTrip(vehicleId, startNodeById, destinationNodeById);
	}
	
	public ShortestPathPlanner getPathPlanner(Set<GraphType> graphTypes){
		if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
			createShortestPathPlanner(graphTypes);
		}
		return shortestPathPlannersMappedByGraphTypes.get(graphTypes);
	}

	private void createShortestPathPlanner(Set<GraphType> graphTypes) {
		ShortestPathPlanner planner = shortestPathPlannerFactory.create(graphTypes);
		
		shortestPathPlannersMappedByGraphTypes.put(graphTypes, planner);
	}
	
}
