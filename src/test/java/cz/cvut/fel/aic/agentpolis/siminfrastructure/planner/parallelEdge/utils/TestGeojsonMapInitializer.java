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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.parallelEdge.utils;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.MapInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.SimulationEdgeFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.SimulationNodeFactory;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.graphimporter.GraphCreator;
import cz.cvut.fel.aic.graphimporter.geojson.GeoJSONReader;
import java.io.File;

/**
 *
 * @author matal
 */
public class TestGeojsonMapInitializer  extends MapInitializer{
	
	private final Transformer projection;
	protected String edgePath;
        protected String nodePath;
	
	
	@Inject
	public TestGeojsonMapInitializer(Transformer projection, AgentpolisConfig config) {
		super(config);
		this.projection = projection;
                String base = new File("").getAbsolutePath()+"/test_astar/";
                nodePath = base+"nodes.geojson";
                edgePath = base+"edges1.geojson";
	}
	
	

	@Override
	protected Graph<SimulationNode, SimulationEdge> getGraph() {                                		
		String serializedGraphFile = config.pathToSerializedGraph;
                
		GeoJSONReader importer = new GeoJSONReader(edgePath, nodePath, serializedGraphFile, projection);

		// beware that the simplifiction is alredy done in python preprocessing an is broken in java
		GraphCreator<SimulationNode, SimulationEdge> graphCreator = new GraphCreator(
				true, false, importer, new SimulationNodeFactory(), new SimulationEdgeFactory());

		return graphCreator.getMap();
	}
	
}