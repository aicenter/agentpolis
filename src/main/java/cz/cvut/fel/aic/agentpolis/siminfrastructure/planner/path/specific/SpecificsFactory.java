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
