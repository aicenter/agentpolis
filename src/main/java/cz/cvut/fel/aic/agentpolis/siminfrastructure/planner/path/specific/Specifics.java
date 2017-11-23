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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.specific;

import java.util.Set;

/**
 * Provides unified interface for operations that are different in directed
 * graphs and in undirected graphs.
 */
public abstract class Specifics<VV, EE>
{
    /**
     * Returns the edges outgoing from the specified vertex in case of
     * directed graph, and the edge touching the specified vertex in case of
     * undirected graph.
     *
     * @param vertex the vertex whose outgoing edges are to be returned.
     *
     * @return the edges outgoing from the specified vertex in case of
     * directed graph, and the edge touching the specified vertex in case of
     * undirected graph.
     */
    public abstract Set<? extends EE> edgesOf(VV vertex);
}