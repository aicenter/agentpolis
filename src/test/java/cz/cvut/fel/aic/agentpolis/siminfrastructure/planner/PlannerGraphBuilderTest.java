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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;



public class PlannerGraphBuilderTest {

//	List<Node> metroLine1;
//	List<Node> metroLine2;
//	List<Node> path;
//	Node node1;
//	Node node2;
//
//	@Before
//	public void initGraph() {
//		node1 = new Node(0, null, "A1");
//		node2 = new Node(1, null, "A2");
//		Node node3 = new Node(2, null, "A3");
//
//		Edge edge1 = new Edge(0, node1, node2, null, new double[] { 1.0 });
//		Edge edge2 = new Edge(1, node2, node1, null, new double[] { 1.0 });
//
//		Edge edge3 = new Edge(2, node2, node3, null, new double[] { 1.0 });
//		Edge edge4 = new Edge(3, node3, node2, null, new double[] { 1.0 });
//
//		node1.getOutgoingEdges().put(edge1.getId(), edge1);
//		node1.getIncomingEdges().put(edge2.getId(), edge2);
//
//		node2.getOutgoingEdges().put(edge2.getId(), edge2);
//		node2.getOutgoingEdges().put(edge3.getId(), edge3);
//		node2.getIncomingEdges().put(edge1.getId(), edge1);
//		node2.getIncomingEdges().put(edge3.getId(), edge4);
//
//		node3.getOutgoingEdges().put(edge4.getId(), edge4);
//		node3.getIncomingEdges().put(edge3.getId(), edge3);
//
//		// ------------------------
//
//		Node node4 = new Node(3, null, "B1");
//		Node node5 = new Node(4, null, "B2");
//		Node node6 = new Node(5, null, "B3");
//
//		Edge edge5 = new Edge(4, node4, node5, null, new double[] { 1.0 });
//		Edge edge6 = new Edge(5, node5, node4, null, new double[] { 1.0 });
//
//		Edge edge7 = new Edge(6, node5, node6, null, new double[] { 1.0 });
//		Edge edge8 = new Edge(7, node6, node5, null, new double[] { 1.0 });
//
//		node4.getOutgoingEdges().put(edge5.getId(), edge5);
//		node4.getIncomingEdges().put(edge6.getId(), edge6);
//
//		node5.getOutgoingEdges().put(edge6.getId(), edge6);
//		node5.getOutgoingEdges().put(edge7.getId(), edge7);
//		node5.getIncomingEdges().put(edge5.getId(), edge5);
//		node5.getIncomingEdges().put(edge7.getId(), edge8);
//
//		node6.getOutgoingEdges().put(edge8.getId(), edge8);
//		node6.getIncomingEdges().put(edge7.getId(), edge7);
//
//		// -------------------------
//
//		Node node7 = new Node(6, null, "C");
//		Edge edge9 = new Edge(8, node7, node5, null, new double[] { 1.0 });
//		node7.getOutgoingEdges().put(edge9.getId(), edge9);
//
//		Edge edge10 = new Edge(9, node2, node7, null, new double[] { 1.0 });
//		node2.getOutgoingEdges().put(edge10.getId(), edge10);
//
//		metroLine2 = new ArrayList<Node>();
//		metroLine2.add(node4);
//		metroLine2.add(node5);
//		metroLine2.add(node6);
//
//		metroLine1 = new ArrayList<Node>();
//		metroLine1.add(node1);
//		metroLine1.add(node2);
//		metroLine1.add(node3);
//
//		path = new ArrayList<Node>();
//		path.add(node7);
//
//	}
//
//	@Test
//	public void plannerGraphBuilderTest() {
//		PlannerGraphBuilder plannerGraphBuilder = new PlannerGraphBuilder();
//		Map<PublicTransportType, Collection<Node>> graphsByType = new HashMap<PublicTransportType, Collection<Node>>();
//		graphsByType.put(PublicTransportType.METRO, new ArrayList<Node>(metroLine1));
//		graphsByType.put(PublicTransportType.WALK, new ArrayList<Node>(path));
//
//		Map<Long, PlannerNode> nodes = plannerGraphBuilder.getNodeWithRelationForPlanning(Arrays
//				.asList(PublicTransportType.METRO), graphsByType);
//		assertNotNull(nodes);
//		assertEquals(3, nodes.size());
//
//		assertEquals(1, nodes.get(node1.getId()).getOutgoingEdges().size());
//		assertEquals(3, nodes.get(node2.getId()).getOutgoingEdges().size());
//
//		nodes = plannerGraphBuilder.getNodeWithRelationForPlanning(Arrays.asList(PublicTransportType.METRO,
//				PublicTransportType.WALK), graphsByType);
//		assertNotNull(nodes);
//		assertEquals(4, nodes.size());
//
//	}
}
