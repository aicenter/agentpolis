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
package cz.cvut.fel.aic.agentpolis.simpresentationlayer;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simpresentationlayer.support.GraphInitializer;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.Collection;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestCorrectDistance {

	@Test
	public void run() {

		Graph<SimulationNode, SimulationEdge> graph = GraphInitializer.getGraphForTest();

		Collection<SimulationEdge> edges = graph.getAllEdges();

		Iterator<SimulationEdge> iter = edges.iterator();

		assertEquals(503.7, iter.next().shape.getShapeLength(), 2 );
		assertEquals(213.0, iter.next().shape.getShapeLength(), 0.5 );
		assertEquals(259.1, iter.next().shape.getShapeLength(),1 );
		assertEquals(167.9, iter.next().shape.getShapeLength(),0.5 );
		assertEquals(759.9, iter.next().shape.getShapeLength(),3 );
		assertEquals(372.0, iter.next().shape.getShapeLength(),0.75 );
	}
}
