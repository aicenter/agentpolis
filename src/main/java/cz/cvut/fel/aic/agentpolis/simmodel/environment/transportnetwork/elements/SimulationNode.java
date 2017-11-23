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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Node;

/**
 * For RoadEdgeExtended. Allows full control for AgentPolis needs. Same as RoadNode
 *
 * @author Zdenek Bousa
 */
public class SimulationNode extends Node {
    
    public SimulationNode(int id, long osmId, int latE6, int lonE6, int projectedLat, int projectedLon, int elevation) {
        super(id, osmId, latE6, lonE6, projectedLat, projectedLon, elevation);
    }

    public SimulationNode(int id, long sourceId, GPSLocation location) {
        super(id, sourceId, location);
    }
}
