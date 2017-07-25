package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.specific;

import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.traverse.CrossComponentIterator;

/**
 * An implementation of {@link Specifics} for a directed graph.
 */
public class DirectedSpecifics<VV, EE>
    extends Specifics<VV, EE>
{
    private DirectedGraph<VV, EE> graph;

    /**
     * Creates a new DirectedSpecifics object.
     *
     * @param g the graph for which this specifics object to be created.
     */
    public DirectedSpecifics(DirectedGraph<VV, EE> g)
    {
        graph = g;
    }

    /**
     * @see CrossComponentIterator.Specifics#edgesOf(Object)
     */
    @Override
    public Set<? extends EE> edgesOf(VV vertex)
    {
        return graph.outgoingEdgesOf(vertex);
    }
}