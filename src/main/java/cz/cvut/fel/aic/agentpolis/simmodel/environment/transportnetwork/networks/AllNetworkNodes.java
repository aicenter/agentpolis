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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import java.util.Map;

/**
 * The data wrapper for all nodes in networks and its provider
 */
@Singleton
public class AllNetworkNodes {

	private Map<Integer, SimulationNode> allNetworkNodes;
	public Map<Integer, SimulationNode> getAllNetworkNodes() {
		return allNetworkNodes;
	}

	public void setAllNetworkNodes(Map<Integer, SimulationNode> allNetworkNodes) {
		this.allNetworkNodes = allNetworkNodes;
	}

	@Inject
	public AllNetworkNodes() {
	}

	public AllNetworkNodes(Map<Integer, SimulationNode> allNetworkNodes) {
		super();
		this.allNetworkNodes = allNetworkNodes;
	}

	public SimulationNode getNode(int nodeId) {
		return allNetworkNodes.get(nodeId);
	}
}
