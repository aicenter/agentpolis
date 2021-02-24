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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Node;
import cz.cvut.fel.aic.geographtools.util.Transformer;

/**
 * For RoadEdgeExtended. Allows full control for AgentPolis needs. Same as RoadNode
 *
 * @author Zdenek Bousa
 */
public class SimulationNode extends Node {
	
	/**
	 * Index for node matrices, like distance matrix
	 */
	private final int index;

	public int getIndex() {
		return index;
	}
	
	public SimulationNode(int id, long osmId, int latE6, int lonE6, int projectedLat, int projectedLon, int elevation,
			int index) {
		super(id, osmId, latE6, lonE6, projectedLat, projectedLon, elevation);
		this.index = index;
	}

	public SimulationNode(int id, long sourceId, GPSLocation location, int index) {
		super(id, sourceId, location);
		this.index = index;
	}
	
	public SimulationNode(int id, long sourceId, double latitude, double longitude, int elevation, 
			Transformer transformer, int index){
		super(id, sourceId, latitude, longitude, elevation, transformer);
		this.index = index;
	}
	
	public SimulationNode(int id, long sourceId, int latitudeProjected, int longitudeProjected, int elevation, 
			Transformer transformer, int index){
		super(id, sourceId, latitudeProjected, longitudeProjected, elevation, transformer);
		this.index = index;
	}
}
