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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork;

import java.io.Serializable;

import com.google.common.collect.ImmutableMap;
import org.locationtech.jts.geom.Coordinate;

/**
 * 
 * The class wraps projected coordinates into spatial. The suppose that input
 * coordinates will be in system supported meter unit
 * 
 * @author Zbynek Moler
 * 
 */
public class NodeCoordinateInformation implements Serializable {

	private static final long serialVersionUID = -8889069232786102814L;
	
	// long = node id, coordinate = in cartesian
	private final ImmutableMap<Long, Coordinate> nodesInCartesian;

	public NodeCoordinateInformation(ImmutableMap<Long, Coordinate> nodesInCartesian) {
		super();
		this.nodesInCartesian = nodesInCartesian;
	}

	public Coordinate getNodeInCartesian(long nodeId) {
		return nodesInCartesian.get(nodeId);
	}

	public double getDistanceInMeters(long currentNodeId, long goalNodeId) {
		return nodesInCartesian.get(currentNodeId).distance(nodesInCartesian.get(goalNodeId));
	}

}
