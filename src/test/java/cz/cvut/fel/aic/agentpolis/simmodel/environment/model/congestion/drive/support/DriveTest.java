/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.CongestedDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.CongestionTestType;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fido
 */
public class DriveTest {
	
	private static final int TEN_MINUTES_IN_MILIS = 600000;
	
	// we expect trips to be no longer then 40 minutes
	private static final int TRIP_MAX_DURATION = 2400000;
	
	private static final int START_TIME_MILIS = 25200000;
	
	
	private final Injector injector;

	public Injector getInjector() {
		return injector;
	}

	public DriveTest() {
		AgentPolisInitializer agentPolisInitializer = new AgentPolisInitializer(new TestModule());
		injector = agentPolisInitializer.initialize();
	}
	
	
	
	
	
	
	public void run(Graph<SimulationNode, SimulationEdge> graph, Trip<SimulationNode> ... trips) {
		
		
//		AgentpolisConfig config = Configuration.load(new AgentpolisConfig());
		
		//config overwrite
//		config.agentpolis.simulationDurationInMillis = TEN_MINUTES_IN_MILIS;
//		config.agentpolis.startTime = START_TIME_MILIS;
//		config.agentpolis.showVisio = true;
//		Common.setTestResultsDir(config, "test");
		
		SimulationCreator creator = injector.getInstance(SimulationCreator.class);

		// prepare map, entity storages...
		creator.prepareSimulation(getMapData(graph));
		
		CongestedDriveFactory congestedDriveFactory = injector.getInstance(CongestedDriveFactory.class);
		DriveAgentStorage driveAgentStorage = injector.getInstance(DriveAgentStorage.class);
		
		
		for (int i = 0; i < trips.length; i++) {
			Trip<SimulationNode> trip = trips[i];
			PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle" + i, CongestionTestType.TEST_VEHICLE, 2, 
			   EGraphType.HIGHWAY, graph.getNode(0), 15);
		
			DriveAgent driveAgent = new DriveAgent("Test driver" + i, graph.getNode(0));

			driveAgentStorage.addEntity(driveAgent);

			congestedDriveFactory.runActivity(driveAgent, vehicle, trip);
		}
		
		creator.startSimulation();
	}
	
	private MapData getMapData(Graph<SimulationNode, SimulationEdge> graph){
		Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
		graphs.put(EGraphType.HIGHWAY, graph);
		
		Map<Integer, SimulationNode> nodes = createAllGraphNodes(graphs);

		return new MapData(graphs, nodes);
	}
	
	/**
	 * Build map data
	 */
	private Map<Integer, SimulationNode> createAllGraphNodes(Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType) {

		Map<Integer, SimulationNode> nodesFromAllGraphs = new HashMap<>();

		for (GraphType graphType : graphByGraphType.keySet()) {
			Graph<SimulationNode, SimulationEdge> graphStorageTmp = graphByGraphType.get(graphType);
			for (SimulationNode node : graphStorageTmp.getAllNodes()) {
				nodesFromAllGraphs.put(node.getId(), node);
			}

		}

		return nodesFromAllGraphs;
	}
}
