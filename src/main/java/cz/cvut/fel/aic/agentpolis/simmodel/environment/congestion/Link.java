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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.utils.CollectionUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author fido
 */
public class Link {

	final CongestionModel congestionModel;

	/**
	 * Lanes mapped by next nodes
	 */
	private final Map<SimulationNode, Lane> lanesMappedByNodes;

	final SimulationEdge edge;

	final SimulationNode toNode;

	final SimulationNode fromNode;

	final Connection fromConnection;

	final Connection toConnection;

	public SimulationEdge getEdge() {
		return edge;
	}


	private Lane laneForTripEnd;


	public Link(CongestionModel congestionModel, SimulationEdge edge, SimulationNode fromNode,
				SimulationNode targetNode, Connection fromConnection, Connection toConnection) {
		this.congestionModel = congestionModel;
		this.edge = edge;
		this.toNode = targetNode;
		this.fromNode = fromNode;
		this.fromConnection = fromConnection;
		this.toConnection = toConnection;
		this.lanesMappedByNodes = new HashMap<>();
	}


	public int getLaneCount() {
		return lanesMappedByNodes.size();
	}

	public double getLength() {
		return edge.shape.getShapeLength();
	}

	public Lane getLaneByNextNode(SimulationNode node) {
		return lanesMappedByNodes.get(node);
	}

	void startDriving(VehicleTripData vehicleData) {
		Trip<SimulationNode> trip = vehicleData.getTrip();
		Lane nextLane = null;
		if (trip.isEmpty()) {
			nextLane = getLaneForTripEnd();
			vehicleData.setTripFinished(true);
		} else {
			SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
			nextLane = getLaneByNextNode(nextLocation);
		}

		nextLane.startDriving(vehicleData);
	}

	void addLane(Lane lane, SimulationNode nextNode) {
		if (laneForTripEnd == null) {
			laneForTripEnd = lane;
		}
		lanesMappedByNodes.put(nextNode, lane);
	}

	Lane getLaneForTripEnd() {
		return laneForTripEnd;
	}

	private Lane getRandomLane() {
		Entry<SimulationNode, Lane> randomEntry
				= CollectionUtil.getRandomEntryFromMap(lanesMappedByNodes, congestionModel.getRandom());
		return randomEntry.getValue();
	}


	public Collection<Lane> getLanes() {
		return lanesMappedByNodes.values();
	}

}
