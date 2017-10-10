/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author fido
 */
public class TestCrossroad {

    @Test
    public void run() {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        SimulationNode node0 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        SimulationNode node1 = new SimulationNode(1, 0, 0, 0, 0, 10000, 0);
        SimulationNode node2 = new SimulationNode(2, 0, 0, 0, 10000, 10000, 0);
        SimulationNode node3 = new SimulationNode(3, 0, 0, 0, 0, 20000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        graphBuilder.addNode(node3);

        SimulationEdge edge1 = new SimulationEdge(0, 1,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)),null);
        SimulationEdge edge2 = new SimulationEdge(1, 0,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node0)),null);
        SimulationEdge edge3 = new SimulationEdge(1, 2,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)),null);
        SimulationEdge edge4 = new SimulationEdge(2, 1,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node1)),null);
        SimulationEdge edge5 = new SimulationEdge(1, 3,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node3)),null);
        SimulationEdge edge6 = new SimulationEdge(3, 1,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node3, node1)),null);

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        graphBuilder.addEdge(edge5);
        graphBuilder.addEdge(edge6);

        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();

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

        Log.close();
    }
}
