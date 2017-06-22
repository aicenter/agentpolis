/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.GraphBuilder;
import cz.agents.multimodalstructures.additional.ModeOfTransport;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fido
 */
public class Utils {
    public static GraphBuilder<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount) {
        Set<ModeOfTransport> permittedMode = new HashSet<>();
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
        
        int radius = 1000;
        
        for (int i = 0; i < nodeCount; i++) {
            double angle  = 2 * Math.PI / nodeCount * i;
            
            int x = (int) Math.round(radius * Math.cos(angle));
            int y = (int) Math.round(radius * Math.sin(angle));
            
            
            SimulationNode node = new SimulationNode(i, 0, x, y, x, y, 0, false, false);
        
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
