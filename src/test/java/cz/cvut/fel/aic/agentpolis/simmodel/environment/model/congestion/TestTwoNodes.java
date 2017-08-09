/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import org.junit.Test;

/**
 *
 * @author fido
 */
public class TestTwoNodes {
    
    @Test(expected = ModelConstructionFailedException.class)  
    public void run() throws Throwable{
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        
        SimulationNode node1 = new SimulationNode(0, 0, 0, 0, 0, 0, 0);
        SimulationNode node2 = new SimulationNode(1, 0, 100, 0, 0, 0, 0);
        
        graphBuilder.addNode(node1);
        graphBuilder.addNode(node2);
        
        SimulationEdge edge1 = new SimulationEdge(0, 1, 0, 0, 0, 100, 40, 1);
        
        graphBuilder.addEdge(edge1);
        
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);
        
        Log.close();
    }
}
