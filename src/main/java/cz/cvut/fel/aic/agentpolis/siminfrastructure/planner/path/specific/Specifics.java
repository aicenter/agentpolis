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