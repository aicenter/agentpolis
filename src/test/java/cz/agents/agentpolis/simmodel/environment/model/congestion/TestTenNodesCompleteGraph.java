/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

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
public class TestTenNodesCompleteGraph {
    
    @Test
    public void run(){
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = getCompleteGraph(10);
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);
    }

    private GraphBuilder<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount) {
        Set<ModeOfTransport> permittedMode = new HashSet<>();
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        
        for (int i = 0; i < nodeCount; i++) {
            SimulationNode node = new SimulationNode(i, 0, 0, 0, 0, 0, 0, false, false);
        
            graphBuilder.addNode(node);
            
            for (int j = 0; j < i; j++) {
                SimulationEdge edge1 = new SimulationEdge(i, j, 0, 0, 0, 100, permittedMode, 40, 1);
                SimulationEdge edge2 = new SimulationEdge(j, i, 0, 0, 0, 100, permittedMode, 40, 1);

                graphBuilder.addEdge(edge1);
                graphBuilder.addEdge(edge2);
            }
        }
        
        return graphBuilder;
    }
}
