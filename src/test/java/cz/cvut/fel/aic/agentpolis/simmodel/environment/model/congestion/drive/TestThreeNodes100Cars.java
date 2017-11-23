/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author fido
 */
public class TestThreeNodes100Cars {


    @Test
    public void run() throws Throwable {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        SimulationNode node0 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        SimulationNode node1 = new SimulationNode(1, 0, 0, 0, 10000, 10000, 0);
        SimulationNode node2 = new SimulationNode(2, 0, 0, 0, 20000, 20000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);

        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)));
        SimulationEdge edge2 = new SimulationEdge(1, 0, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node0)));
        SimulationEdge edge3 = new SimulationEdge(1, 2, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)));
        SimulationEdge edge4 = new SimulationEdge(2, 1, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node1)));


        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);

        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();

        Trip<SimulationNode>[] trips = new Trip[100];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node0, node1, node2);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trips);

        Log.close();
    }

    public static void main(String[] args) {
        VisualTests.runVisualTest(TestThreeNodes100Cars.class);
    }
}
