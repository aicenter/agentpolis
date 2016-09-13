package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import com.google.common.collect.SetMultimap;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 *
 * @author Marek Cuchy
 *
 */
public class ComplexGraphBuilderTest {

	private ComplexGraphBuilder builder;
	public static final GPSLocation EMPTY_GPS = new GPSLocation(0, 0, 0, 0, 0);

	@Before
    public void beforeTest() {
		builder = new ComplexGraphBuilder();
        builder.addNode(0, 0, EMPTY_GPS);
        builder.addNode(1, 1, EMPTY_GPS);

        builder.addEdge(0, 1, 3, 123, EGraphType.PEDESTRIAN);
        builder.addSharedEdge(0, 1, EGraphType.PEDESTRIAN, EGraphType.HIGHWAY, 1);
        builder.addSharedEdge(0, 1, EGraphType.PEDESTRIAN, EGraphType.BUSWAY, 2);
        builder.addEdge(0, 1, 5, 1223, EGraphType.RAILWAY);
    }

    @Test
    public void testBuildGraphByType() throws Exception {
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType = builder
                .buildGraphByType();

        Graph<SimulationNode, SimulationEdge> busGraph = graphByType.get(EGraphType.BUSWAY);
        SimulationNode busNode0 = busGraph.getNode(0);
        SimulationNode busNode1 = busGraph.getNode(1);
        SimulationEdge busEdge = busGraph.getEdge(0, 1);

        for (GraphType type : new GraphType[] { EGraphType.PEDESTRIAN, EGraphType.HIGHWAY }) {
            Graph<SimulationNode, SimulationEdge> graph = graphByType.get(type);
            assertTrue(graph.getNode(0) == busNode0);
            assertTrue(graph.getNode(1) == busNode1);
            assertTrue(graph.getEdge(0, 1) == busEdge);
        }

        assertEquals(3, busEdge.getLaneCount(EGraphType.PEDESTRIAN));
        assertEquals(1, busEdge.getLaneCount(EGraphType.HIGHWAY));
        assertEquals(2, busEdge.getLaneCount(EGraphType.BUSWAY));
        assertEquals(123, busEdge.length, 0);

        GraphType[][] graphTypes = createGraphTypes(busEdge.getGraphTypes());
        assertEquals(2, graphTypes.length);
        GraphType[] expected = new GraphType[] { EGraphType.PEDESTRIAN, EGraphType.HIGHWAY,
                EGraphType.BUSWAY };

        Arrays.sort(expected);
        if (graphTypes[0].length == 3) {
            Arrays.sort(graphTypes[0]);
            assertArrayEquals(expected, graphTypes[0]);
            assertEquals(graphTypes[1][0], EGraphType.RAILWAY);
        } else {
            Arrays.sort(graphTypes[1]);
            assertArrayEquals(expected, graphTypes[1]);
            assertEquals(graphTypes[0][0], EGraphType.RAILWAY);
        }
    }

    @Test
    public void testRemoveEdge() throws Exception {
        builder.removeEdge(0, 1, EGraphType.PEDESTRIAN);
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType = builder
                .buildGraphByType();
        Graph<SimulationNode, SimulationEdge> busGraph = graphByType.get(EGraphType.BUSWAY);
        SimulationEdge busEdge = busGraph.getEdge(0, 1);
        GraphType[][] graphTypes = createGraphTypes(busEdge.getGraphTypes());
        GraphType[] expected = new GraphType[] { EGraphType.HIGHWAY, EGraphType.BUSWAY };

        Arrays.sort(expected);
        if (graphTypes[0].length == 2) {
            Arrays.sort(graphTypes[0]);
            assertArrayEquals(expected, graphTypes[0]);
            assertEquals(graphTypes[1][0], EGraphType.RAILWAY);
        } else {
            Arrays.sort(graphTypes[1]);
            assertArrayEquals(expected, graphTypes[1]);
            assertEquals(graphTypes[0][0], EGraphType.RAILWAY);
        }
    }

    @Test
    public void testRemoveCompleteEdge() throws Exception {
        builder.addNode(2, 2, EMPTY_GPS);
        builder.addEdge(0, 2, 5, 456, EGraphType.BUSWAY);
        builder.removeEdge(0, 1, EGraphType.PEDESTRIAN);
        builder.removeEdge(0, 1, EGraphType.HIGHWAY);
        builder.removeEdge(0, 1, EGraphType.BUSWAY);
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType = builder
                .buildGraphByType();
        Graph<SimulationNode, SimulationEdge> busGraph = graphByType.get(EGraphType.BUSWAY);
        SimulationEdge busEdge = busGraph.getEdge(0, 1);
        assertNull(busEdge);
        assertNull(busGraph.getNode(1));

    }

    private GraphType[][] createGraphTypes(SetMultimap<GraphType, GraphType> modes) {
        Set<GraphType> used = new HashSet<>();
        List<Set<GraphType>> sets = new ArrayList<>();
        for (GraphType type : modes.keySet()) {
            if (!used.contains(type)) {
                Set<GraphType> types = modes.get(type);
                sets.add(types);
                used.addAll(types);
            }
        }
        GraphType[][] result = new GraphType[sets.size()][];
        for (int i = 0; i < result.length; i++) {
            Set<GraphType> set = sets.get(i);
            result[i] = set.toArray(new GraphType[set.size()]);
        }

        return result;
    }
}
