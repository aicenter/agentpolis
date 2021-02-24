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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.NearestElementUtil;
import cz.cvut.fel.aic.geographtools.util.NearestElementUtilPair;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;

/**
 *
 * @author fido
 */
@Singleton
public class NearestElementUtils {
	private final HashMap<GraphType, NearestElementUtil<SimulationNode>> nearestElementUtilsMappedByGraphType;
	
	private final TransportNetworks transportNetworks;
	
	private final Transformer transformer;
	
//	private final Map<GraphType,Network> networksMappedByGraphType;

	 //TODO clear constructor usage Tranformer injection or not?
	@Inject
	public NearestElementUtils(TransportNetworks transportNetworks,Transformer transformer) {
		this.transportNetworks = transportNetworks;
		this.transformer = transformer;
//		this.networksMappedByGraphType = new HashMap<>();
		this.nearestElementUtilsMappedByGraphType = new HashMap<>();
	}
	
	public SimulationNode getNearestElement(GPSLocation location, GraphType graphType){
		if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
			createNearestElementUtil(graphType);
		}
		
		NearestElementUtil<SimulationNode> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
		
		return nearestElementUtil.getNearestElement(location);
	}
	
	public SimulationNode[] getNearestElements(GPSLocation location, GraphType graphType, int numberOfNearestElements){
		if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
			createNearestElementUtil(graphType);
		}
		
		NearestElementUtil<SimulationNode> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
		
		return nearestElementUtil.getKNearestElements(location, numberOfNearestElements);
	}

	private void createNearestElementUtil(GraphType graphType) {
		List<NearestElementUtilPair<Coordinate,SimulationNode>> pairs = new ArrayList<>();
		
		for (SimulationNode node : transportNetworks.getGraph(graphType).getAllNodes()) {
			pairs.add(new NearestElementUtilPair<>(new Coordinate(node.getLongitude(), node.getLatitude()), node));
		}
		
		NearestElementUtil<SimulationNode> nearestElementUtil = new NearestElementUtil<>(pairs, transformer, 
				new NodeArrayConstructor());
		
		nearestElementUtilsMappedByGraphType.put(graphType, nearestElementUtil);
	}
	
	private static class NodeArrayConstructor implements NearestElementUtil.SerializableIntFunction<SimulationNode[]>{

		@Override
		public SimulationNode[] apply(int value) {
			return new SimulationNode[value];
		}

	}
	
	
	
}
