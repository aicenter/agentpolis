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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork;

import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
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
