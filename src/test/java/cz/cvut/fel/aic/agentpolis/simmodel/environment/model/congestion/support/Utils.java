/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
                SimulationEdge edge1 = new SimulationEdge(i, j, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(i), graphBuilder.getNode(j))));
                SimulationEdge edge2 = new SimulationEdge(j, i, 0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(j), graphBuilder.getNode(i))));

                graphBuilder.addEdge(edge1);
                graphBuilder.addEdge(edge2);
            }
        }

        return graphBuilder;
    }
}
