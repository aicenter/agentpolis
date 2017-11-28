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
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.PrepareDummyLanes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.*;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fido
 */
public class TestCrossroad {
    private Graph<SimulationNode, SimulationEdge> graph;
    private SimulationNode node1, node2, node3, node0;

    @Before
    public void prepare() {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        node0 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        node1 = new SimulationNode(1, 0, 0, 0, 0, 10000, 0);
        node2 = new SimulationNode(2, 0, 0, 0, 10000, 10000, 0);
        node3 = new SimulationNode(3, 0, 0, 0, 0, 20000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        graphBuilder.addNode(node3);

        List<LinkedList<Lane>> lanes = PrepareDummyLanes.getLanesTwo();

        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)), lanes.get(0));
        SimulationEdge edge2 = new SimulationEdge(1, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node0)), lanes.get(1));
        SimulationEdge edge3 = new SimulationEdge(1, 2, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)), lanes.get(2));
        SimulationEdge edge4 = new SimulationEdge(2, 1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node1)), lanes.get(3));
        SimulationEdge edge5 = new SimulationEdge(1, 3, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node3)), lanes.get(4));
        SimulationEdge edge6 = new SimulationEdge(3, 1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node3, node1)), lanes.get(5));

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        graphBuilder.addEdge(edge5);
        graphBuilder.addEdge(edge6);

        graph = graphBuilder.createGraph();
    }

    @Test
    public void run() {
        Trip<SimulationNode>[] trips = new Trip[20];

        for (int i = 0; i < trips.length / 2; i++) {
            Trip<SimulationNode> trip = new Trip<>(node0, node1, node3);
            trips[i] = trip;
        }

        for (int i = trips.length / 2; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node2, node1, node3);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trips);
    }

    @After
    public void after() {
        Log.close();
    }

    public static void main(String[] args) {
        VisualTests.runVisualTest(TestCrossroad.class);
    }
}
