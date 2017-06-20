/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.drive;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.environment.model.congestion.*;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.GraphBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author fido
 */
public class TestThreeNodes {
    
    @Test 
    public void run() throws Throwable{
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        
        SimulationNode node1 = new SimulationNode(0, 0, 0, 0, 0, 0, 0, false, false);
        SimulationNode node2 = new SimulationNode(1, 0, 100, 0, 0, 0, 0, false, false);
        SimulationNode node3 = new SimulationNode(2, 0, 100, 0, 0, 0, 0, false, false);
        
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        graphBuilder.addNode(node3);
        
        Set<ModeOfTransport> permittedMode = new HashSet<>();
        
        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 0, 100, permittedMode, 40, 1);
        SimulationEdge edge2 = new SimulationEdge(1, 0, 0, 0, 0, 100, permittedMode, 40, 1);
        SimulationEdge edge3 = new SimulationEdge(1, 2, 0, 0, 0, 100, permittedMode, 40, 1);
        SimulationEdge edge4 = new SimulationEdge(2, 1, 0, 0, 0, 100, permittedMode, 40, 1);
        
        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        Trip<SimulationNode> trip = new Trip<>(node1, node2, node3);
        
        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trip);
    }
}
