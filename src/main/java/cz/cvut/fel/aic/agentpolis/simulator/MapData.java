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
package cz.cvut.fel.aic.agentpolis.simulator;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.Map;

/**
 * The wrapper of data created by {@code MapInitFactory}
 * 
 * @author Zbynek Moler
 * 
 */
public class MapData {

	public final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;
	public final Map<Integer,SimulationNode> nodesFromAllGraphs;

	public MapData(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType,
			Map<Integer, SimulationNode> nodesFromAllGraphs) {
		super();
		this.graphByType = graphByType;
		this.nodesFromAllGraphs = nodesFromAllGraphs;
	}

}
