/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.Transformer;

import java.util.Arrays;

/**
 * @author fido
 */
public class Utils {
    public static GraphBuilder<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount) {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        int radius = 1000;

        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI / nodeCount * i;

            int x = (int) Math.round(radius * Math.cos(angle));
            int y = (int) Math.round(radius * Math.sin(angle));


            SimulationNode node = new SimulationNode(i, 0, x, y, x, y, 0);

            graphBuilder.addNode(node);

            for (int j = 0; j < i; j++) {
                SimulationEdge edge1 = new SimulationEdge(i, j, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(i), graphBuilder.getNode(j))),null);
                SimulationEdge edge2 = new SimulationEdge(j, i,  0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(j), graphBuilder.getNode(i))),null);

                graphBuilder.addEdge(edge1);
                graphBuilder.addEdge(edge2);
            }
        }

        return graphBuilder;
    }
}
