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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.Edge;
import java.math.BigInteger;

import java.util.List;

/**
 * Extended RoadEdge, contains road situation (lanes,two way pairs,...)
 * Also provides extended identification of the edge.
 *
 * @author Zdenek Bousa
 */
public class SimulationEdge extends Edge<SimulationNode> {
	/**
	 * unique ID for each edge, also recognize directions
	 */
	private final int id;
	
	/**
	 * Static id of this edge. It should be consistent, even if the edge is loaded from a different map/file.
	 */
	private final BigInteger staticId;

	/**
	 * -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
	 */
	private final int oppositeWayId; // unique edge id that is in opposite direction, otherwise -1 (one-way)

	/**
	 * TODO: lanes
	 * Lanes turn				   lanesTurn{left,through|through,right}
	 * Lanes continues to edges:	lanesContinuesToEdge = {{1,2},{2,3}}
	 * Lanes count				  lanesCount = 2
	 */
	private final int lanesCount;
	private List<Lane> lanesTurn; // not implemented // TODO: lanes turning

	/**
	 * maximal allowed speed in meters per second
	 */
	private final int allowedMaxSpeedInKmh;

	public final EdgeShape shape;

	
	
	
	public BigInteger getStaticId() {
		return staticId;
	}
	
	public int getAllowedMaxSpeedInKmh() {
		return allowedMaxSpeedInKmh;
	}
	
	public double getAllowedMaxSpeedInCmPerSecond(){
		return (double) getAllowedMaxSpeedInKmh() / 3.6 * 1E2;
	}
	


	public SimulationEdge(SimulationNode fromNode,
						  SimulationNode toNode,
						  int id,
						  int oppositeWayId,
						  int lengthInCentimeters,
						  int allowedMaxSpeedInKmh,
						  int lanesCount,
						  EdgeShape edgeShape) {
		this(fromNode, toNode, null, id, oppositeWayId, lengthInCentimeters, allowedMaxSpeedInKmh, lanesCount, edgeShape);
	}
	
	/**
	 * @param fromNode			 source node
	 * @param toNode			   destination node
	 * @param staticId			 osm id of this edge
	 * @param id
	 * @param allowedMaxSpeedInKmh maximal allowed speed in kilometers per hour
	 * @param lengthInCentimeters	   -
	 * @param oppositeWayId		-1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway).
	 *							 Input should be correct, it is not validated!
	 * @param lanesCount		   total number of lanes for ModeOfTransport-car
	 * @param edgeShape			instance of EdgeShape representing the shape of the road edge
	 */
	public SimulationEdge(SimulationNode fromNode,
						  SimulationNode toNode,
						  BigInteger staticId,
						  int id,
						  int oppositeWayId,
						  int lengthInCentimeters,
						  int allowedMaxSpeedInKmh,
						  int lanesCount,
						  EdgeShape edgeShape) {
		super(fromNode, toNode, lengthInCentimeters);

		this.id = id;
		this.allowedMaxSpeedInKmh = allowedMaxSpeedInKmh;
		this.staticId = staticId;

		if (oppositeWayId >= -1) {
			this.oppositeWayId = oppositeWayId;
		} else {
			this.oppositeWayId = -2;
		}

		if (lanesCount >= 1) {
			this.lanesCount = lanesCount;
		} else {
			this.lanesCount = 1; //minimum
		}

		this.shape = edgeShape;
	}

	/**
	 * ID
	 *
	 * @return unique id of the edge
	 */
	public int getUniqueId() {
		return id;
	}

	/**
	 * Information about opposite direction on the road.
	 *
	 * @return -1 if it is oneway, -2 for unknown or ID of the opposite direction edge (twoway)
	 */
	public int getOppositeWayId() {
		return oppositeWayId;
	}

	/**
	 * Information about number of lanes for cars.
	 *
	 * @return total number of lanes (minimum is 1)
	 */
	public int getLanesCount() {
		return lanesCount;
	}

	public String getLogInfo() {
		return fromNode.getId() + "-" + toNode.getId();
	}
}
