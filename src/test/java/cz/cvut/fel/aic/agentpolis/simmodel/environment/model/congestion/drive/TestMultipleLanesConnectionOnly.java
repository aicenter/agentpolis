package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.*;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Zdenek Bousa
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMultipleLanesConnectionOnly {
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

        //Lanes
        LinkedList<Lane> lanes1 = new LinkedList<>();
        Lane lane1 = new Lane(0, 0);
        lane1.addDirection(-1, LaneTurnDirection.unknown);
        lanes1.add(lane1);
        Lane lane12 = new Lane(1, 0);
        lane12.addDirection(-1, LaneTurnDirection.unknown);
        lanes1.add(lane12);

        LinkedList<Lane> lanes2 = new LinkedList<>();
        Lane lane2 = new Lane(2, 1);
        lane2.addDirection(-1, LaneTurnDirection.unknown);
        lanes2.add(lane2);
        Lane lane21 = new Lane(3, 1);
        lane21.addDirection(-1, LaneTurnDirection.unknown);
        lanes2.add(lane21);


        LinkedList<Lane> lanes3 = new LinkedList<>();
        Lane lane3 = new Lane(4, 2);
        lane3.addDirection(-1, LaneTurnDirection.unknown);
        lanes3.add(lane3);
        Lane lane31 = new Lane(5, 2);
        lane31.addDirection(-1, LaneTurnDirection.unknown);
        lanes3.add(lane31);


        LinkedList<Lane> lanes4 = new LinkedList<>();
        Lane lane4 = new Lane(6, 3);
        lane4.addDirection(-1, LaneTurnDirection.unknown);
        lanes4.add(lane4);
        Lane lane41 = new Lane(7, 3);
        lane41.addDirection(-1, LaneTurnDirection.unknown);
        lanes4.add(lane41);

        LinkedList<Lane> lanes5 = new LinkedList<>();
        Lane lane5 = new Lane(8, 4);
        lane5.addDirection(-1, LaneTurnDirection.unknown);
        lanes5.add(lane5);
        Lane lane51 = new Lane(9, 4);
        lane51.addDirection(-1, LaneTurnDirection.unknown);
        lanes5.add(lane51);

        LinkedList<Lane> lanes6 = new LinkedList<>();
        Lane lane6 = new Lane(10, 5);
        lane6.addDirection(-1, LaneTurnDirection.unknown);
        lanes6.add(lane6);
        Lane lane61 = new Lane(11, 5);
        lane61.addDirection(-1, LaneTurnDirection.unknown);
        lanes6.add(lane61);


        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node0, node1)), lanes1);
        SimulationEdge edge2 = new SimulationEdge(1, 2, 1, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node1, node2)), lanes2);
        SimulationEdge edge3 = new SimulationEdge(2, 3, 2, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node2, node3)), lanes3);
        SimulationEdge edge4 = new SimulationEdge(3, 4, 3, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node3, node4)), lanes4);
        SimulationEdge edge5 = new SimulationEdge(4, 5, 4, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node4, node5)), lanes5);
        SimulationEdge edge6 = new SimulationEdge(5, 0, 5, 0, 100, 40, 2, new EdgeShape(Arrays.asList(node5, node0)), lanes6);

        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        graphBuilder.addEdge(edge5);
        graphBuilder.addEdge(edge6);

        graph = graphBuilder.createGraph();
    }

    @Test
    public void run1CarStraight() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[1];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node1, node2, node3, node4);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest(30000);
        driveTest.run(graph, trips);
        DriveAgentStorage a = driveTest.getAgents();

        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == agent.getTargetNode());
        }
    }

    @Test
    public void run1CarStraightLoop() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[1];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node1, node2, node3, node4, node5, node0, node1);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest(61000);
        driveTest.run(graph, trips);
        DriveAgentStorage a = driveTest.getAgents();

        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == agent.getTargetNode());
        }
    }

    @Test
    public void run2Cars() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[2];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node1, node2, node3, node4, node5);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest(38000);
        driveTest.run(graph, trips);
        DriveAgentStorage a = driveTest.getAgents();

        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == agent.getTargetNode());
        }
    }

    @Test
    public void run100Cars() throws Throwable {
        Trip<SimulationNode>[] trips = new Trip[100];

        for (int i = 0; i < trips.length; i++) {
            Trip<SimulationNode> trip = new Trip<>(node1, node2, node3, node4, node5);
            trips[i] = trip;
        }

        DriveTest driveTest = new DriveTest(120000);
        driveTest.run(graph, trips);
        DriveAgentStorage a = driveTest.getAgents();

        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == agent.getTargetNode());
        }
    }

    @After
    public void endTest() {
        Log.close();
    }
}
