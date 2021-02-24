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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.Utils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import org.junit.Test;

/**
 *
 * @author fido
 */
public class TestTenNodesCompleteGraph {
	
	@Test 
	public void run() throws Throwable{
		
		// bootstrap Guice
		DriveTest scenario = new DriveTest();
		Injector injector = scenario.getInjector();
		
		// set roadgraph
		Graph<SimulationNode, SimulationEdge> graph = Utils.getCompleteGraph(10, injector.getInstance(Transformer.class));
		
		CongestionModelTest congestionModelTest = new CongestionModelTest();
		congestionModelTest.run(graph);
		
		Trip<SimulationNode> trip = new Trip<>(0,graph.getNode(0), graph.getNode(5), graph.getNode(9), graph.getNode(3),
		graph.getNode(1), graph.getNode(2), graph.getNode(4), graph.getNode(8), graph.getNode(7), graph.getNode(6));
		
		scenario.run(graph, trip);
	}
	
	public static void main(String[] args) {
		VisualTests.runVisualTest(TestTenNodesCompleteGraph.class);
	}
}
