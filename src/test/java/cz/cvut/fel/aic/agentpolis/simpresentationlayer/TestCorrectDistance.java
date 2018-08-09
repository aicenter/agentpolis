package cz.cvut.fel.aic.agentpolis.simpresentationlayer;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simpresentationlayer.support.GraphInitializer;
import cz.cvut.fel.aic.geographtools.Graph;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

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
