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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.NodeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalNode;

/**
 *
 * @author fido
 */
public class SimulationNodeFactory implements NodeFactory<SimulationNode>{

	@Override
	public SimulationNode createNode(InternalNode internalNode) {

		return new SimulationNode(internalNode.id, internalNode.sourceId, internalNode.latE6, internalNode.lonE6,
				internalNode.getLatitudeProjected1E2(), internalNode.getLongitudeProjected1E2(), internalNode.elevation,
				((Long) internalNode.get("index")).intValue());
	}

}
