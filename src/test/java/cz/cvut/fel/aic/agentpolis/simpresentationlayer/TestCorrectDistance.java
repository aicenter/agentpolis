package cz.cvut.fel.aic.agentpolis.simpresentationlayer;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
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

        assertEquals(980.50, iter.next().shape.getShapeLength(), 0.0001 );
        assertEquals(156.66, iter.next().shape.getShapeLength(), 0.0001 );
        assertEquals(500.03, iter.next().shape.getShapeLength(),0.0001 );
        assertEquals(130.58, iter.next().shape.getShapeLength(),0.0001 );
        assertEquals(1480.26, iter.next().shape.getShapeLength(),0.0001 );
        assertEquals(285.09, iter.next().shape.getShapeLength(),0.0001 );


        Log.close();
    }
}
