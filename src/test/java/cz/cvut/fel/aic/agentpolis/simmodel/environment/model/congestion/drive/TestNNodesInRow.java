/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm.CTMConnection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author fido
 */
public class TestNNodesInRow {

    @Test
    public void run() throws Throwable {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        int N = 101;
        int overallLength = 2000000;
        int projectedOffset = overallLength / (N - 1);
        int segmentLength = overallLength / (N - 1) / 100;

        float v = 30;
        System.out.println("v = " + v);
        System.out.println("h = " + segmentLength);
        System.out.println("deltaT = " + CTMConnection.deltaT / 1000);
        boolean CFLCondition = (v * CTMConnection.deltaT / 1000 < segmentLength);
        System.out.println("Courant-Friedrich-Lewy (CFL) condition v*deltaT < h is " + CFLCondition);

        if(!CFLCondition){
            System.exit(1);
        }
        int uniqueWayId = 0;

        ArrayList<SimulationNode> nodes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            SimulationNode node = new SimulationNode(i, 0, 0, 0, i * projectedOffset, i * projectedOffset, 0);
            graphBuilder.addNode(node);
            nodes.add(node);
        }

        SimulationNode extraNode = new SimulationNode(N, 0, 0, 0, 1000000, 1000000, 0);
        graphBuilder.addNode(extraNode);
        nodes.add(extraNode);
        for (int i = 0; i < N - 1; i++) {
            int lanes = 2;
            if (i == N / 2) {
                lanes = 1;
            }
            SimulationEdge edge = new SimulationEdge(i, i + 1, 0, uniqueWayId++, 0, segmentLength, v, lanes, new EdgeShape(Arrays.asList(nodes.get(i), nodes.get(i + 1))));
            SimulationEdge edgeBack = new SimulationEdge(i + 1, i, 0, uniqueWayId++, 0, segmentLength, v, lanes, new EdgeShape(Arrays.asList(nodes.get(i + 1), nodes.get(i))));
            graphBuilder.addEdge(edge);
            graphBuilder.addEdge(edgeBack);

        }

        SimulationEdge edgeBack1 = new SimulationEdge(N - 1, extraNode.id, 0, uniqueWayId++, 0, segmentLength, v, 1, new EdgeShape(Arrays.asList(nodes.get(N - 1), nodes.get(N))));
        SimulationEdge edgeBack2 = new SimulationEdge(extraNode.id, 0, 0, uniqueWayId++, 0, segmentLength, v, 1, new EdgeShape(Arrays.asList(nodes.get(N), nodes.get(0))));
        graphBuilder.addEdge(edgeBack1);
        graphBuilder.addEdge(edgeBack2);

        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        Trip<SimulationNode>[] trips = new Trip[1000];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(new LinkedList<>(nodes));
            trips[i] = trip;
        }
//        for (int i = trips.length/2; i < trips.length; i++) {
//            Trip<SimulationNode> trip = new Trip<>(new LinkedList<>(nodes.subList(5, nodes.size() - 1)));
//            trips[i] = trip;
//        }
        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trips);

        Log.close();
    }
}
