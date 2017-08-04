/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.DriveTest;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Test;

/**
 *
 * @author fido
 */
public class TestThreeNodes {
    
    @Test 
    public void run() throws Throwable{
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        
        SimulationNode node1 = new SimulationNode(0, 0, 0, 100, 0, 0, 0);
        SimulationNode node2 = new SimulationNode(1, 0, 0, 100, 100, 0, 0);
        SimulationNode node3 = new SimulationNode(2, 0, 0, 100, 200, 0, 0);
        
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        graphBuilder.addNode(node3);
        
        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 0, 100, 40, 1);
        SimulationEdge edge2 = new SimulationEdge(1, 0, 0, 0, 0, 100, 40, 1);
        SimulationEdge edge3 = new SimulationEdge(1, 2, 0, 0, 0, 100, 40, 1);
        SimulationEdge edge4 = new SimulationEdge(2, 1, 0, 0, 0, 100, 40, 1);
        
        graphBuilder.addEdge(edge1);
        graphBuilder.addEdge(edge2);
        graphBuilder.addEdge(edge3);
        graphBuilder.addEdge(edge4);
        
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        Trip<SimulationNode> trip = new Trip<>(node1, node2, node3);
        
        DriveTest driveTest = new DriveTest();
        driveTest.run(graph, trip);
        
        Log.close();
    }
}
