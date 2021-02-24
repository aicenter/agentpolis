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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.parallelEdge;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.parallelEdge.utils.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.init.MapInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 *
 * @author travnja5
 */
public class AstarParallelEdgesTest {
    
        private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AstarParallelEdgesTest.class);
        private final String base = new File("").getAbsolutePath()+"/test_astar/";
        private final String nodePath = base+"nodes.geojson";
        private final String edgePath1 = base+"edges1.geojson";
        private final String edgePath2 = base+"edges2.geojson";
        
    
    
        @Before
        public void before() throws IOException{
                LOGGER.info("Creating test files.");

                File f = new File(base);
                f.mkdirs();

                FileWriter fil = new FileWriter(nodePath);
                fil.write("{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [0.0, 0.0]}, \"properties\": {\"node_id\": \"1\", \"index\": 0}}, {\"type\": \"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [0.01, 0.0]}, \"properties\": {\"node_id\": \"2\", \"index\": 1}}]}");
                fil.close();
                fil = new FileWriter(edgePath1);
                fil.write("{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[0.0, 0.0], [0.01, 0.0]]}, \"properties\": {\"highway\": \"residential\", \"maxspeed\": 40.0, \"id\": \"1\", \"lanes\": 1, \"utm_coords\": [[-652669530, 1058827799], [-652668236, 1058827461]], \"length\": 15, \"from_id\": \"1\", \"to_id\": \"2\"}}]}");
                fil.close();
                fil = new FileWriter(edgePath2);
                //both short and long edge 
                fil.write("{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 2, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[0.0, 0.0],[0.008, 0.0001],[0.01, 0.0]]}, \"properties\": {\"highway\": \"residential\", \"maxspeed\": 40.0, \"id\": \"2\", \"lanes\": 1, \"utm_coords\": [[-652669530, 1058827799], [-652668236, 1058827461]], \"length\": 20, \"from_id\": \"1\", \"to_id\": \"2\"}}, {\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[0.0, 0.0], [0.01, 0.0]]}, \"properties\": {\"highway\": \"residential\", \"maxspeed\": 40.0, \"id\": \"1\", \"lanes\": 1, \"utm_coords\": [[-652669530, 1058827799], [-652668236, 1058827461]], \"length\": 15, \"from_id\": \"1\", \"to_id\": \"2\"}}]}");
                
                //just the longer edge - test should always fail
                //fil.write("{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 2, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[0.0, 0.0],[0.008, 0.0001],[0.01, 0.0]]}, \"properties\": {\"highway\": \"residential\", \"maxspeed\": 40.0, \"id\": \"2\", \"lanes\": 1, \"utm_coords\": [[-652669530, 1058827799], [-652668236, 1058827461]], \"length\": 20, \"from_id\": \"1\", \"to_id\": \"2\"}}]}");
                fil.close();
        }
        
        @After
        public void after(){
                LOGGER.info("Removing test files");

                File f = new File(nodePath);
                f.delete();
                f = new File(edgePath1);
                f.delete();
                f = new File(edgePath2);
                f.delete();
                f = new File(base);
                f.delete();
        }
    
        @Test
        public void test(){                
                AgentpolisConfig config = new AgentpolisConfig();
                TestModuleAstar tma = new TestModuleAstar(config, null);                
                
                Injector injector = new TestAgentPolisInitializer(tma).initialize();

                MapInitializer mapInitializer = injector.getInstance(TestGeojsonMapInitializer.class);
                MapData mapData = mapInitializer.getMap();                
                long singleEdgeSolution = computeSolution(mapData, injector);
                
                injector = new TestAgentPolisInitializer(tma).initialize();
                
                mapInitializer = injector.getInstance(TestGeojsonMapInitializer1.class);
		mapData = mapInitializer.getMap();                
                long parallelEdgesSolution = computeSolution(mapData, injector);

                Assert.assertEquals(singleEdgeSolution, parallelEdgesSolution);
                
                                                        
        }
        
        public long computeSolution(MapData mapData, Injector injector){                	
		injector.getInstance(AllNetworkNodes.class).setAllNetworkNodes(mapData.nodesFromAllGraphs);
		injector.getInstance(Graphs.class).setGraphs(mapData.graphByType);
                Map<Integer, SimulationNode> map = injector.getInstance(AllNetworkNodes.class).getAllNetworkNodes();                                
                TripsUtil tu = injector.getInstance(TripsUtil.class);
                
                return tu.getTripDuration(tu.createTrip(map.get(0), map.get(1)));                
        }
}
