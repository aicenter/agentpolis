/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.multimodal;


public class PlannerMHDAstarTest {

//	@Test
//	public void plannerMHDAstarTest1(){
//	 
//		AgentPolisEnvironment env = mock(AgentPolisEnvironment.class);
//		TimeStorage timeStorage = mock(TimeStorage.class);
//		when(env.getTimeStorage()).thenReturn(timeStorage);
//		when(env.getTimeStorage().getActualTime()).thenReturn(new ZonedDateTime(0));
//		
//		AGraphStorage<TramNode, TramEdge> rail = new AGraphStorage<TramNode, TramEdge>();		
//		GraphBuilder<TramNode, TramEdge> builder = new GraphBuilder<TramNode, TramEdge>();
//		builder.putEdgeDir(new TramEdge(GraphMock.edge12.getId(), GraphMock.edge12.getFrom(), GraphMock.edge12.getTo(), GraphMock.edge23.getLength()));
//		builder.putEdgeDir(new TramEdge(GraphMock.edge45.getId(), GraphMock.edge45.getFrom(), GraphMock.edge45.getTo(), GraphMock.edge45.getLength()));		
//		rail.setGraph(builder.getGraph());
//		when(env.getRailwayGraphStorage()).thenReturn(rail);
//
//		AGraphStorage<StreetNode, StreetEdge> street = new AGraphStorage<StreetNode, StreetEdge>();		
//		GraphBuilder<StreetNode, StreetEdge> sbuilder = new GraphBuilder<StreetNode, StreetEdge>();
//		sbuilder.putEdgeDir(new StreetEdge(GraphMock.edge23.getId(), GraphMock.edge23.getFrom(), GraphMock.edge23.getTo(), GraphMock.edge12.getLength(),""));
//		sbuilder.putEdgeDir(new StreetEdge(GraphMock.edge34.getId(), GraphMock.edge34.getFrom(), GraphMock.edge34.getTo(), GraphMock.edge34.getLength(),""));		
//		street.setGraph(sbuilder.getGraph());
//		when(env.getStreetStorage()).thenReturn(street);
//
//		PublicTransportStorage storage = new PublicTransportStorage();
//
//		
//		List<PublicTransportStation> publicTransportStations = new ArrayList<PublicTransportStation>();
//		PublicTransportStation publicTransportStation = new PublicTransportStation("Station 1",GraphMock.node1,true,true);
//		Set<Duration> leaveTimes = new TreeSet<Duration>();
//		leaveTimes.add(new Duration(2));
//		StationTimeTable timeTable = new StationTimeTable(leaveTimes);
//		publicTransportStation.addTimeTable("Line 1", timeTable);
//		publicTransportStation.addLineId("Line 1");
//		publicTransportStations.add(publicTransportStation);
//		publicTransportStation = new PublicTransportStation("Station 2",GraphMock.node2,true,true);
//		leaveTimes = new TreeSet<Duration>();
//		leaveTimes.add(new Duration(50000));
//		timeTable = new StationTimeTable(leaveTimes);
//		publicTransportStation.addTimeTable("Line 1", timeTable);
//		publicTransportStation.addLineId("Line 1");
//		publicTransportStations.add(publicTransportStation);
//		PublicTransportLine publicTransportLine = new PublicTransportLine("Line 1", "Line 1 back",PublicTransportType.METRO, publicTransportStations, GraphMock.GraphType.GRAPH1 ,0);
//		storage.addLineAndNoReverse(publicTransportLine);
//		
//		publicTransportStations = new ArrayList<PublicTransportStation>();
//		publicTransportStation = new PublicTransportStation("Station 4",GraphMock.node4,true,true);
//		leaveTimes = new TreeSet<Duration>();
//		leaveTimes.add(new Duration(85690790));
//		timeTable = new StationTimeTable(leaveTimes);
//		publicTransportStation.addTimeTable("Line 2", timeTable);
//		publicTransportStation.addLineId("Line 2");
//		publicTransportStations.add(publicTransportStation);
//		publicTransportStation = new PublicTransportStation("Station 5",GraphMock.node5,true,true);
//		leaveTimes = new TreeSet<Duration>();
//		leaveTimes.add(new Duration(90754231));
//		timeTable = new StationTimeTable(leaveTimes);
//		publicTransportStation.addTimeTable("Line 2", timeTable);
//		publicTransportStation.addLineId("Line 2");
//		publicTransportStations.add(publicTransportStation);		
//		publicTransportLine = new PublicTransportLine("Line 2","Line 2 back", PublicTransportType.METRO,publicTransportStations, GraphMock.GraphType.GRAPH1,0 );
//		storage.addLineAndNoReverse(publicTransportLine);
//		
//		when(env.getPublicTransportStorage()).thenReturn(storage);
//		
//		TransportPlanStorage transportPlanStorage = new TransportPlanStorage();
//		transportPlanStorage.init(env, null, null);		
//		when(env.getTransportPlanStorage()).thenReturn(transportPlanStorage);
//		
////		public void addTimeTable(String lineId, StationTimeTable timeTable) {
//		
//		PlannerMHDAstar.init(env);
//		LinkedList<Transfer> transfers = PlannerMHDAstar.findPath(GraphMock.node1.getId(), GraphMock.node5.getId(),EPlannerTypeOfGroupId.LINE_ID);
//		
//	   
//		assertNull(transfers.get(0).lineId); // WALK
//		assertNotNull(transfers.get(1).lineId); // BY METRO 
//		assertNull(null, transfers.get(2).lineId); // WALK 
//		assertNull(null, transfers.get(3).lineId); // WALK 
//		assertNotNull(transfers.get(4).lineId); // BY METRO 
//		
//		
//		
//		
//		
//	}
	
}
