package cz.agents.agentpolis.siminfrastructure.planner.multimodal;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.*;

public class GraphMock {

	public enum EGraphTypeMock implements GraphType {
		GRAPH1;
	}

	public static Node node1 = createNode(0, createGPS(33.819397, -118.195716), "Wardlow Station");

	public static Node node2 = createNode(1, createGPS(33.847566, -118.210822), "Del Amo Station");
	public static Node node3 = createNode(1, createGPS(33.876644, -118.222793), "Artesia Station");
	public static Node node4 = createNode(2, createGPS(33.896314, -118.224232), "Compton Station");
	public static Node node5 = createNode(3, createGPS(33.929046, -118.23805), "Imperial / Wilmington (Rosa " +
			"Parks)Station");

	public static Edge edge12 = new Edge(node1.id, node2.id, 3429);
	public static Edge edge21 = new Edge(node2.id, node1.id, 3429);
	public static Edge edge23 = new Edge(node2.id, node3.id, 3417);
	public static Edge edge32 = new Edge(node3.id, node2.id, 3417);
	public static Edge edge34 = new Edge(node3.id, node4.id, 2191);
	public static Edge edge43 = new Edge(node4.id, node3.id, 2191);
	public static Edge edge45 = new Edge(node4.id, node5.id, 3857);
	public static Edge edge54 = new Edge(node5.id, node4.id, 3857);

	public static Graph<Node, Edge> graph;

	static {
		GraphBuilder<Node, Edge> builder = new GraphBuilder<>();
		builder.addNode(node1);
		builder.addNode(node2);
		builder.addNode(node3);
		builder.addNode(node4);
		builder.addNode(node5);
		builder.addEdge(edge12);
		builder.addEdge(edge21);
		builder.addEdge(edge23);
		builder.addEdge(edge32);
		builder.addEdge(edge34);
		builder.addEdge(edge43);
		builder.addEdge(edge45);
		builder.addEdge(edge54);

		graph = builder.createGraph();
	}

	private static GPSLocation createGPS(double lat, double lon) {
		return new GPSLocation(lat, lon, 0, 0, 0);
	}

	private static Node createNode(int id, GPSLocation gps, String s) {
		return new SimulationNode(id, id, gps);
	}

}
