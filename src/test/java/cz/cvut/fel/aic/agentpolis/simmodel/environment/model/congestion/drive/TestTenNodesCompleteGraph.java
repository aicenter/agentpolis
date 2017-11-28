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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.PrepareDummyLanes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.Utils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;


/**
 * @author fido
 */
public class TestTenNodesCompleteGraph {

    @Test
    public void run() throws Throwable {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = Utils.getCompleteGraph(10);
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();

        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);

        Trip<SimulationNode> trip = new Trip<>(graph.getNode(0), graph.getNode(5), graph.getNode(9), graph.getNode(3),
                graph.getNode(1), graph.getNode(2), graph.getNode(4), graph.getNode(8), graph.getNode(7), graph.getNode(6));

        DriveTest driveTest = new DriveTest(30000);
        driveTest.run(graph, trip);

        DriveAgentStorage a = driveTest.getAgents();
        Assert.assertTrue(a != null);

        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == trip.getLocations().getLast());
        }
    }

    @After
    public void after() {
        Log.close();
    }

    public static void main(String[] args) {
        VisualTests.runVisualTest(TestTenNodesCompleteGraph.class);
    }
}
