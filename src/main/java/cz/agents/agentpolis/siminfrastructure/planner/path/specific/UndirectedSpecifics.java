package cz.agents.agentpolis.siminfrastructure.planner.path.specific;

import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.traverse.CrossComponentIterator;

/**
 * An implementation of {@link Specifics} in which edge direction (if any)
 * is ignored.
 */
public class UndirectedSpecifics<VV, EE>
    extends Specifics<VV, EE>
{
    private Graph<VV, EE> graph;

    /**
     * Creates a new UndirectedSpecifics object.
     *
     * @param g the graph for which this specifics object to be created.
     */
    public UndirectedSpecifics(Graph<VV, EE> g)
    {
        graph = g;
    }

    /**
     * @see CrossComponentIterator.Specifics#edgesOf(Object)
     */
    @Override
    public Set<EE> edgesOf(VV vertex)
    {
        return graph.edgesOf(vertex);
    }
}
