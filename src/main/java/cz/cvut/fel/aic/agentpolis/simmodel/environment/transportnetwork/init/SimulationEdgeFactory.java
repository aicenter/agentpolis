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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.EdgeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalEdge;
import java.math.BigInteger;
import java.util.List;

public class SimulationEdgeFactory implements EdgeFactory<SimulationNode,SimulationEdge> {

	@Override
	public SimulationEdge createEdge(InternalEdge internalEdge, GraphBuilder<SimulationNode,SimulationEdge> graphBuilder) {
		List<GPSLocation> coordinatesList = internalEdge.get("coordinateList");
		EdgeShape edgeShape = new EdgeShape(coordinatesList);
		SimulationNode fromNode = graphBuilder.getNode(internalEdge.getFromNode().id);
		SimulationNode toNode = graphBuilder.getNode(internalEdge.getToNode().id);
		return new SimulationEdge(fromNode, toNode, new BigInteger(((String) internalEdge.get("id"))),
				internalEdge.get("uniqueWayID"), internalEdge.get("oppositeWayUniqueId"), internalEdge.getLengthCm(),
				internalEdge.get("allowedMaxSpeedInMpS"),
				internalEdge.get("lanesCount"), edgeShape);
	}

}
