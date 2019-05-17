/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.specific;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

public class SpecificsFactory {
	public static <V, E> Specifics<V, E> create(Graph<V, E> g)
	{
		if (g instanceof DirectedGraph<?, ?>) {
			return new DirectedSpecifics<V, E>((DirectedGraph<V, E>) g);
		} else {
			return new UndirectedSpecifics<V, E>(g);
		}
	}
}
