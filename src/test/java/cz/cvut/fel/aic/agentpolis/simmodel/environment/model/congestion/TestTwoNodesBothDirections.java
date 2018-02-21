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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion;

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.PrepareDummyLanes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
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
public class TestTwoNodesBothDirections {
    private Graph<SimulationNode, SimulationEdge> graph;

    @Before
    public void prepare(){
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        SimulationNode node1 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        SimulationNode node2 = new SimulationNode(1, 0, 100, 0, 0, 0, 0);

        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);

        List<LinkedList<Lane>> lanes = PrepareDummyLanes.getLanes(2,2);

        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)), lanes.get(0));
        SimulationEdge edge2 = new SimulationEdge(1, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node1)), lanes.get(1));

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);

        graph = graphBuilder.createGraph();
    }

    @Test
    public void run() throws Throwable {
        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);
    }

    @After
    public void after(){
        Log.close();
    }

    public static void main(String[] args) {
        VisualTests.runVisualTest(TestTwoNodesBothDirections.class);
    }
}
