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

        int N = 21;
        int overallLength = 200000;
        int projectedOffset = overallLength / (N - 1);
        int segmentLength = overallLength / (N - 1)/100;
        ArrayList<SimulationNode> nodes = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            SimulationNode node = new SimulationNode(i, 0, 0, 0, i * projectedOffset, i * projectedOffset, 0);
            graphBuilder.addNode(node);
            nodes.add(node);
        }
        for (int i = 0; i < N - 1; i++) {
            int lanes = 4;
            if(i==5){
                lanes = 4;
            }
            SimulationEdge edge = new SimulationEdge(i, i + 1, 0, 0, 0, segmentLength, 40, lanes, new EdgeShape(Arrays.asList(nodes.get(i), nodes.get(i + 1))));
            graphBuilder.addEdge(edge);

        }

        SimulationEdge edgeBack = new SimulationEdge(N - 1, 0, 0, 0, 0, overallLength/100, 40, 1, new EdgeShape(Arrays.asList(nodes.get(N - 1), nodes.get(0))));
        graphBuilder.addEdge(edgeBack);

        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        Trip<SimulationNode>[] trips = new Trip[100];

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
