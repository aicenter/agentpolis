/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveAgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.Utils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author fido
 */
public class TestTenNodesCompleteGraph {

    @Test
    public void run() throws Throwable {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = Utils.getCompleteGraph(10);
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();

        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);

        Trip<SimulationNode> trip = new Trip<>(graph.getNode(0), graph.getNode(5), graph.getNode(9), graph.getNode(3),
                graph.getNode(1), graph.getNode(2), graph.getNode(4), graph.getNode(8), graph.getNode(7), graph.getNode(6));

        DriveTest driveTest = new DriveTest(30000);
        DriveAgentStorage a = driveTest.getAgents();
        Assert.assertTrue(a != null);
        for (DriveAgent agent : a.getEntities()) {
            Assert.assertTrue(agent.getId() + "did not make it to its target node.", agent.getPosition() == agent.getTargetNode());
        }

        Log.close();
    }
}
