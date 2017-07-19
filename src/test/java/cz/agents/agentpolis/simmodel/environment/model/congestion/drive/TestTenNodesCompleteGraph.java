/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.drive;

import cz.agents.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.agents.agentpolis.simmodel.environment.model.congestion.support.Utils;
import cz.agents.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.environment.model.congestion.*;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.GraphBuilder;
import org.junit.Test;

/**
 *
 * @author fido
 */
public class TestTenNodesCompleteGraph {
    
    @Test 
    public void run() throws Throwable{
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = Utils.getCompleteGraph(10);
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);
        
        Trip<SimulationNode> trip = new Trip<>(graph.getNode(0), graph.getNode(5), graph.getNode(9), graph.getNode(3),
        graph.getNode(1), graph.getNode(2), graph.getNode(4), graph.getNode(8), graph.getNode(7), graph.getNode(6));
        
        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trip);
    }
}
