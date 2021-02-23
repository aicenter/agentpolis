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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NearestElementUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author fido
 */
@Singleton
public class TripsUtil {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TripsUtil.class);
	
	protected static final Set<GraphType> HIGHWAY_GRAPH_TYPES = new HashSet(Arrays.asList(EGraphType.HIGHWAY));


	protected final ShortestPathPlanner pathPlanner;
	
	protected final IdGenerator tripIdGenerator;

	private final NearestElementUtils nearestElementUtils;
	private final Graph<SimulationNode, SimulationEdge> network;


	
	
	@Inject
	public TripsUtil(ShortestPathPlanner pathPlanner, NearestElementUtils nearestElementUtils, HighwayNetwork network,IdGenerator tripIdGenerator) {
		this.pathPlanner = pathPlanner;
		this.nearestElementUtils = nearestElementUtils;
		this.network = network.getNetwork();
		this.tripIdGenerator = tripIdGenerator;
	}
	
	
	
	public Trip<SimulationNode> createTrip(SimulationNode fromNode, SimulationNode toNode, Set<GraphType> graphTypes) {
		if (fromNode == toNode) {
			try {
				throw new Exception("Start node cannot be the same as end node");
			} catch (Exception ex) {
				LOGGER.error(null, ex);
			}
		}
		
		return new Trip<>(tripIdGenerator.getId(), pathPlanner.findShortestPath(fromNode, toNode, graphTypes));
	}
	
	public Trip<SimulationNode> createTrip(SimulationNode fromNode, SimulationNode toNode, GraphType graphType) {
		return createTrip(fromNode, toNode, new HashSet(Arrays.asList(graphType)));
	}
	
	public Trip<SimulationNode> createTrip(SimulationNode fromNode, SimulationNode toNode) {
		return createTrip(fromNode, toNode, HIGHWAY_GRAPH_TYPES);
	}
	
	public VehicleTrip createTrip(SimulationNode fromNode, SimulationNode toNode, PhysicalVehicle vehicle) {
		Trip trip = createTrip(fromNode, toNode, HIGHWAY_GRAPH_TYPES);
		VehicleTrip vehicleTrip = new VehicleTrip<>(tripIdGenerator.getId(),vehicle, trip.getLocations());
		return vehicleTrip;
	}

	@Deprecated
	public static VehicleTrip mergeTripsOld(VehicleTrip<SimulationNode>... trips) {	
		SimulationNode[] locations = Arrays.stream(trips).map(Trip::getLocations).toArray(SimulationNode[]::new);		
		return new VehicleTrip(trips[0].getTripId(),trips[0].getVehicle(), locations);
	}

	/**
	 * Computes trip minimum duration in milliseconds.
	 * @param trip Trip
	 * @return Trip duration in milliseconds.
	 */
	public long getTripDuration(Trip<SimulationNode> trip) {
		long duration = 0;

		SimulationNode[] locations = trip.getLocations();
		if (locations.length >= 2) {
			Node startNode = locations[0];
			for (int i = 1; i < locations.length; i++) {
				Node targetNode = locations[i];
				SimulationEdge edge = network.getEdge(startNode, targetNode);
				duration += MoveUtil.computeMinDuration(edge);
				startNode = targetNode;
			}
		}

		return duration;
	}

	public int[] getLocationIndexes(Trip<SimulationNode> trip){
		int[] indexes = new int[trip.getLocations().length];
		int i = 0;
		for(SimulationNode location: trip.getLocations()){
			indexes[i] = location.getIndex();
			i++;
		}
		return indexes;
	}
	
	public String printLocationIndexes(Trip<SimulationNode> trip){
		int[] indexes = getLocationIndexes(trip);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		String locString = Arrays.stream(indexes).mapToObj(String::valueOf).collect(Collectors.joining(", "));
		sb.append(locString).append(")");
		return sb.toString();
	}
}
