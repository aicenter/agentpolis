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

import cz.cvut.fel.aic.geographtools.Edge;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;




/**
 * 
 * 
 * The representation of a particular transport network
 * 
 * @author Zbynek Moler
 * 
 * @param <TNode>
 * @param <TEdge>
 */
public abstract class Network<TNode extends Node, TEdge extends Edge> {

	private final Graph<TNode, TEdge> network;

	public Network(Graph<TNode, TEdge> network) {
		super();
		this.network = network;
	}

	public Graph<TNode, TEdge> getNetwork() {
		return network;
	}
}
