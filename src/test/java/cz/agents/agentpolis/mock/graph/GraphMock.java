package cz.agents.agentpolis.mock.graph;

import java.util.Map;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.ComplexGraphBuilder;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;
import cz.agents.basestructures.GPSLocation;

public class GraphMock {

	public enum EGraphTypeMock implements GraphType {
		GRAPH1;
	}

	public static Node node1 = createNode(0, createGPS(33.819397, -118.195716), "Wardlow Station");

	public static Node node2 = createNode(1, createGPS(33.847566, -118.210822), "Del Amo Station");
	public static Node node3 = createNode(2, createGPS(33.876644, -118.222793), "Artesia Station");
	public static Node node4 = createNode(3, createGPS(33.896314, -118.224232), "Compton Station");
	public static Node node5 = createNode(4, createGPS(33.929046, -118.23805), "Imperial / Wilmington (Rosa " + "Parks)Station");

	public static Edge edge12 = new Edge(node1.id, node2.id, 3429);
	public static Edge edge21 = new Edge(node2.id, node1.id, 3429);
	public static Edge edge23 = new Edge(node2.id, node3.id, 3417);
	public static Edge edge32 = new Edge(node3.id, node2.id, 3417);
	public static Edge edge34 = new Edge(node3.id, node4.id, 2191);
	public static Edge edge43 = new Edge(node4.id, node3.id, 2191);
	public static Edge edge45 = new Edge(node4.id, node5.id, 3857);
	public static Edge edge54 = new Edge(node5.id, node4.id, 3857);

	public static Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;

	static {

		ComplexGraphBuilder builder = new ComplexGraphBuilder();

		builder.addNode(node1);
		builder.addNode(node2);
		builder.addNode(node3);
		builder.addNode(node4);
		builder.addNode(node5);

		addEdge(builder, edge12, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge21, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge23, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge32, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge34, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge43, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge45, EGraphTypeMock.GRAPH1);
		addEdge(builder, edge54, EGraphTypeMock.GRAPH1);

		addEdge(builder, edge12, EGraphType.PEDESTRIAN);
		addEdge(builder, edge45, EGraphType.PEDESTRIAN);

		addEdge(builder, edge23, EGraphType.HIGHWAY);
		addEdge(builder, edge34, EGraphType.HIGHWAY);

		graphByType = builder.buildGraphByType();
	}

	private static void addEdge(ComplexGraphBuilder builder, Edge edge, GraphType type) {
		builder.addEdge(edge.fromId, edge.toId, 1, edge.length, type);
	}

	private static GPSLocation createGPS(double lat, double lon) {
		return new GPSLocation(lat, lon, 0, 0, 0);
	}

	private static Node createNode(int id, GPSLocation gps, String s) {
		return new SimulationNode(id, id, gps);
	}
}
