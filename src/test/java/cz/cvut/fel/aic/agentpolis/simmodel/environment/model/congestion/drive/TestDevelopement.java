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
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zdenek Bousa
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDevelopement {
    private Graph<SimulationNode, SimulationEdge> graph;
    private SimulationNode node0, node1, node2, node3, node4, node5;

    @Before
    public void startTest() throws Throwable {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        node0 = new SimulationNode(0, 0, 0, 0, 0, 20000, 0);
        node1 = new SimulationNode(1, 1, 0, 0, 10000, 10000, 0);
        node2 = new SimulationNode(2, 2, 0, 0, 10000, 20000, 0);
        node3 = new SimulationNode(3, 3, 0, 0, 10000, 30000, 0);
        node4 = new SimulationNode(4, 3, 0, 0, 10000, 40000, 0);
        node5 = new SimulationNode(5, 3, 0, 0, 10000, 50000, 0);

        graphBuilder.addNode(node0);
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        graphBuilder.addNode(node3);
        graphBuilder.addNode(node4);
        graphBuilder.addNode(node5);

        List<LinkedList<Lane>> lanes = PrepareDummyLanes.getLanes(6,1);

        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node0, node1)), lanes.get(0));
        SimulationEdge edge2 = new SimulationEdge(1, 2, 1, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node2)), lanes.get(1));
        SimulationEdge edge3 = new SimulationEdge(2, 3, 2, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node3)), lanes.get(2));


        // Other way
        SimulationEdge edge4 = new SimulationEdge(3, 2, 3, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node3, node2)), lanes.get(3));
        SimulationEdge edge5 = new SimulationEdge(2, 1, 4, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node2, node1)), lanes.get(4));
        SimulationEdge edge6 = new SimulationEdge(1, 0, 5, 0, 100, 40, 1, new EdgeShape(Arrays.asList(node1, node0)), lanes.get(5));

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        graphBuilder.addEdge(edge5);
        graphBuilder.addEdge(edge6);

        graph = graphBuilder.createGraph();
    }

    @Test //@Ignore
    public void carLoop() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[2];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node0,node1, node2, node3);
            trips[i] = trip;
        }
        SimulationNode goal = trips[0].getLocations().getLast();
        DriveTest driveTest = new DriveTest(61000);
        driveTest.run(graph, trips);
        DriveAgentStorage a = driveTest.getAgents();

        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == goal);
        }
    }


    @After
    public void endTest() {
        Log.close();
    }

    public static void main(String[] args) throws NoSuchMethodException {
        VisualTests.runVisualTest(TestDevelopement.class);
    }
}
