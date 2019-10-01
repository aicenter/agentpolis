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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.TestCongestionModel;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Connection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Crossroad;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.geographtools.Graph;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author fido
 */
public class CongestionModelTest {
	
	private final Injector injector;

	public Injector getInjector() {
		return injector;
	}

	public CongestionModelTest() {
		AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer();
		agentPolisInitializer.overrideModule(new TestModule());
		injector = agentPolisInitializer.initialize();
	}
	
	
	
	public void run(Graph<SimulationNode, SimulationEdge> graph) throws Throwable{
		
		Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
		graphs.put(EGraphType.HIGHWAY, graph);
		
		//map init
		injector.getInstance(Graphs.class).setGraphs(graphs);
		
		try{
			TestCongestionModel congestionModel = injector.getInstance(TestCongestionModel.class);
			Graph<SimulationNode,SimulationEdge> roadGraph = injector.getInstance(HighwayNetwork.class).getNetwork();

			nodeTest(congestionModel, roadGraph);
			edgeTest(congestionModel, roadGraph);
		}
		catch(ProvisionException e){
			throw e.getCause();
		}
	}

	private void nodeTest(TestCongestionModel congestionModel, Graph<SimulationNode, SimulationEdge> roadGraph) {
		for (SimulationNode simulationNode : roadGraph.getAllNodes()) {
			Connection connection = congestionModel.getConnectionByNode(simulationNode);
			
			//each node have a connection test
			assertNotNull(connection);
			
			if(connection instanceof Crossroad){
				checkCrossroad((Crossroad) connection);
			}
			else{
				checkConnection(connection);
			}
		}
	}
	
	private void edgeTest(TestCongestionModel congestionModel, Graph<SimulationNode, SimulationEdge> roadGraph) {
		for (SimulationEdge simulationEdge : roadGraph.getAllEdges()) {
			Link link = congestionModel.getLinkByEdge(simulationEdge);
			
			// each edge have a link test
			assertNotNull("Edge " + simulationEdge.getLogInfo()+ " does not have a corresponding link!",link);
			
			// no empty links test
			assertTrue("Link " + simulationEdge.getLogInfo()+ " has no lanes!", 
					link.getLaneCount() > 0);
			
			// check all next nodes has a lane
			checkLaneForEachNextNode(roadGraph, link);
		}
	}

	private void checkLaneForEachNextNode(Graph<SimulationNode, SimulationEdge> roadGraph, Link link) {
		SimulationNode targetNode = link.getEdge().toNode;
		List<SimulationEdge> outEdges = roadGraph.getOutEdges(targetNode);
		
		for (SimulationEdge outEdge : outEdges) {
			SimulationNode nextNode = outEdge.toNode;
			
			assertNotNull("No lane for output edge " + outEdge.getLogInfo()+ "!", link.getLaneByNextNode(nextNode));
		}
	}

	private void checkConnection(Connection connection) {
		assertNotNull(connection.getNextLink((Connection) null));
	}

	private void checkCrossroad(Crossroad crossroad) {
		assertTrue(crossroad.getNumberOfInputLanes() > 0);
	}
}
