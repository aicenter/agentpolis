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

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.TestGraphCreator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import org.junit.Test;


/**
 *
 * @author fido
 */
public class TestThreeNodes2Cars {
	
	
	
	@Test 
	public void run() throws Throwable{
		DriveTest driveTest = new DriveTest();
                Graph<SimulationNode, SimulationEdge> graph = TestGraphCreator.create3x1Line(driveTest);
                
		SimulationNode node0 = graph.getNode(0);
		SimulationNode node1 = graph.getNode(1);
		SimulationNode node2 = graph.getNode(2);
		
		Trip<SimulationNode>[] trips = new Trip[2];
		
		for(int i = 0; i < trips.length; i++){
			Trip<SimulationNode> trip = new Trip<>(i,node0, node1, node2);
			trips[i] = trip;
		}
		
		driveTest.run(graph, trips);
	}
	
	public static void main(String[] args) {
		VisualTests.runVisualTest(TestThreeNodes2Cars.class);
	}
}
