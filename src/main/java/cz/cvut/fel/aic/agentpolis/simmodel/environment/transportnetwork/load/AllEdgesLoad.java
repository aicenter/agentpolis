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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.load;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.utils.CollectionUtil;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @param <E>
 * @param <ES>
 * @author fido
 */
public class AllEdgesLoad<E extends AgentPolisEntity & MovingEntity, ES extends EntityStorage<E>>
		implements Iterable<Entry<BigInteger, Integer>> {

	protected final ES entityStorage;

	private final HashMap<BigInteger, Integer> loadPerEdge;

	protected final Graph<SimulationNode, SimulationEdge> network;


	public Iterable<Integer> loadsIterable = new Iterable<Integer>() {
		@Override
		public Iterator<Integer> iterator() {
			return loadPerEdge.values().iterator();
		}
	};

//	public ArrayList<Integer> test;


	public HashMap<BigInteger, Integer> getLoadPerEdge() {
		return loadPerEdge;
	}


	@Inject
	public AllEdgesLoad(ES entityStorage, HighwayNetwork highwayNetwork) {
		this.entityStorage = entityStorage;
		loadPerEdge = new HashMap<>();
		network = highwayNetwork.getNetwork();
	}


	@Inject // this annotation is necessary because this method has to be called after child constructor finishes
	public void compute() {
		for (E entity : entityStorage) {
			String entityId = entity.getId();
			Node currentNode = entity.getPosition();
			Node targetNode = entity.getTargetNode();
			if (targetNode != null && targetNode != currentNode) {
				BigInteger edgeId = network.getEdge(currentNode, targetNode).getStaticId();
				countLoadForPosition(entityId, edgeId);
			}
		}
	}

	public int getLoadPerEdge(BigInteger staticID) {
		if (loadPerEdge.containsKey(staticID)) {
			return loadPerEdge.get(staticID);
		}
		return 0;
	}

	public int getNumberOfEdges() {
		return network.getAllEdges().size();
	}


	@Override
	public Iterator<Entry<BigInteger, Integer>> iterator() {
		return loadPerEdge.entrySet().iterator();
	}


	protected void countLoadForPosition(String entityId, BigInteger edgeId) {
		CollectionUtil.incrementMapValue(loadPerEdge, edgeId, 1);
	}


}
