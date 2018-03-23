package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.VisualTests;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.PrepareDummyLanes;
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
 * Tests that simple connection transferees only valid lanes (with correct id of following edge - direction)
 * @author Zdenek Bousa
 */
public class TestConnectionSimple {
    private Graph<SimulationNode, SimulationEdge> graph;
    private SimulationNode node1, node2, node0;

    @Before
    public void prepare() {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        node0 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        node1 = new SimulationNode(1, 0, 0, 0, 0, 10000, 0);
        node2 = new SimulationNode(2, 0, 0, 0, 10000, 10000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);

        List<LinkedList<Lane>> lanes = PrepareDummyLanes.getLanes(4,2);

        SimulationEdge edge0 = new SimulationEdge(0, 1, 0, -1, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)), lanes.get(0));
        SimulationEdge edge1 = new SimulationEdge(1, 2, 1, -1, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)), lanes.get(1));
        SimulationEdge edge2 = new SimulationEdge(2, 0, 2, -1, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node0)), lanes.get(2));

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge0);

        graph = graphBuilder.createGraph();
    }

    @Test
    public void run() {
        Trip<SimulationNode>[] trips = new Trip[100];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node0, node1, node2);
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
        VisualTests.runVisualTest(TestConnectionSimple.class);
    }
}
