/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.simmodel.environment.model.congestion.support.Utils;
import cz.agents.agentpolis.simmodel.environment.model.congestion.support.CongestionModelTest;
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
    public void run() throws Throwable{
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = Utils.getCompleteGraph(10);
        Graph<SimulationNode, SimulationEdge> graph = graphBuilder.createGraph();
        
        CongestionModelTest congestionModelTest = new CongestionModelTest();
        congestionModelTest.run(graph);
    }
    
}

    
