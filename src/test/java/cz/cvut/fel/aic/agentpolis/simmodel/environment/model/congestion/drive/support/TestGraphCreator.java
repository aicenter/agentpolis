/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.Utils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import java.util.Arrays;

/**
 *
 * @author travnja5
 */
public class TestGraphCreator {
        public static Graph<SimulationNode, SimulationEdge> create3x1Line(DriveTest driveTest){                
                return Utils.getGridGraph(3, driveTest.getInjector().getInstance(Transformer.class), 1);
        }
        
        public static Graph<SimulationNode, SimulationEdge> createTshapedCrossroad(DriveTest driveTest){
            GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();
                int GRID = 5000;
                Transformer projection = driveTest.getInjector().getInstance(Transformer.class);
                
                SimulationNode node0 = new SimulationNode(0, 0, 0, 0, 0,  projection, 0);
		SimulationNode node1 = new SimulationNode(1, 0, 0, GRID, 0, projection, 0);
		SimulationNode node2 = new SimulationNode(2, 0, -GRID, GRID, 0, projection, 0);
		SimulationNode node3 = new SimulationNode(3, 0, 0, 2*GRID,0, projection, 0);

		graphBuilder.addNode(node0);
		graphBuilder.addNode(node1);
		graphBuilder.addNode(node2);
		graphBuilder.addNode(node3);

		SimulationEdge edge1 = new SimulationEdge(node0, node1, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node0, node1)));
		SimulationEdge edge2 = new SimulationEdge(node1, node0, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node1, node0)));
		SimulationEdge edge3 = new SimulationEdge(node1, node2, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node1, node2)));
		SimulationEdge edge4 = new SimulationEdge(node2, node1, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node2, node1)));
		SimulationEdge edge5 = new SimulationEdge(node1, node3, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node1, node3)));
		SimulationEdge edge6 = new SimulationEdge(node3, node1, 0, 0, GRID, 40, 1, new EdgeShape(Arrays.asList(node3, node1)));

		graphBuilder.addEdge(edge1);
		graphBuilder.addEdge(edge2);
		graphBuilder.addEdge(edge3);
		graphBuilder.addEdge(edge4);
		graphBuilder.addEdge(edge5);
		graphBuilder.addEdge(edge6);
                
                return graphBuilder.createGraph();            
        }
}
