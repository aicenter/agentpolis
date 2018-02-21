/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.PrepareDummyLanes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fido
 */
public class TestThreeNodesWithSimpleConnection {
    private Graph<SimulationNode, SimulationEdge> graph;
    private SimulationNode node0,node1,node2;

    @Before
    public void prepare(){
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        node0 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        node1 = new SimulationNode(1, 0, 0, 0, 10000, 10000, 0);
        node2 = new SimulationNode(2, 0, 0, 0, 20000, 20000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);

        List<LinkedList<Lane>> lanes = PrepareDummyLanes.getLanes(3,2);

        SimulationEdge edge1 = new SimulationEdge(0, 1, 0,  0, 100, 40, 2, new EdgeShape(Arrays.asList(node0, node1)),lanes.get(0));
        SimulationEdge edge2 = new SimulationEdge(2, 0, 0, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node2, node0)),lanes.get(1));
        SimulationEdge edge3 = new SimulationEdge(1, 2,  0, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node1, node2)),lanes.get(2));


        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);

        graph = graphBuilder.createGraph();
    }

    @Test
    public void hundredCars() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[100];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node0, node1, node2);
            trips[i] = trip;
        }
        SimulationNode goal = trips[0].getLocations().getLast();
        DriveTest driveTest = new DriveTest(70000);
        driveTest.run(graph, trips);

        DriveAgentStorage a = driveTest.getAgents();
        Assert.assertNotNull(a);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() ==  goal);
        }
    }

    @After
    public void after(){
        Log.close();
    }

    public static void main(String[] args) {
        VisualTests.runVisualTest(TestThreeNodesWithSimpleConnection.class);
    }
}
