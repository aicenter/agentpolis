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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author david
 */
@Singleton
public class NodesMappedByIndex {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NodesMappedByIndex.class);
	
	private final Map<Integer,SimulationNode> nodesMappedByIndex;

	@Inject
	public NodesMappedByIndex(AllNetworkNodes nodes) {
		nodesMappedByIndex = new HashMap<>();
		for(SimulationNode node: nodes.getAllNetworkNodes().values()){
			nodesMappedByIndex.put(node.getIndex(), node);
		}
	}
	
	public SimulationNode getNodeByIndex(int index){
		try{
			SimulationNode node = nodesMappedByIndex.get(index);
			if(node == null){
				throw new Exception(String.format("Node with index %s does not exist", index));
			}
			return nodesMappedByIndex.get(index);
		}
		catch(Exception ex){
			Logger.getLogger(NodesMappedByIndex.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
			
	}
	
}
