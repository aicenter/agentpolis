package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity.movement;

import org.junit.Test;

public class EntityMoveTest {

    
    
    @Test
    public void test1(){
//        Node node = new Node(1, new LatLon(1.0, 1.0), null);
//        Node node1 = new Node(2, new LatLon(2.0, 2.0), null);
//        Map<Long, Node> nodesFromAllGraphs =  new HashMap<Long, Node>();
//        nodesFromAllGraphs.put(new Long(1), node);
//        nodesFromAllGraphs.put(new Long(2), node1);
//        
//        
//        
//        EventProcessor eventProcessor = mock(EventProcessor.class);
//        MaxEntitySpeedStorage entitySpeedStorage = mock(MaxEntitySpeedStorage.class);
//        when(entitySpeedStorage.getEntityMaxSpeed("id")).thenReturn(100.0);
//        
//        
//        EntityMovementGE entityMove = new EntityMovementGE(nodesFromAllGraphs,eventProcessor,entitySpeedStorage);
//        
//        Set<String> entityIds = new HashSet<String>();
//        entityIds.add("id");
//        
//        EntityPositionStorage entityPositionStorage = mock(EntityPositionStorage.class);
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(1));
//        
//        Map<String,Coordinate> map=  entityMove.getEntityPosition(entityIds, entityPositionStorage,regionBounds);
//        assertEquals(1, map.size());
        
        
        
    }
    
    @Test
    public void test2(){
//        Node node = new Node(1, new LatLon(1.0, 1.0), null);
//        Node node1 = new Node(2, new LatLon(2.0, 2.0), null);
//        Map<Long, Node> nodesFromAllGraphs =  new HashMap<Long, Node>();
//        nodesFromAllGraphs.put(new Long(1), node);
//        nodesFromAllGraphs.put(new Long(2), node1);
//        
//        
//        EventProcessor eventProcessor = mock(EventProcessor.class);
//        MaxEntitySpeedStorage entitySpeedStorage = mock(MaxEntitySpeedStorage.class);
//        when(entitySpeedStorage.getEntityMaxSpeed("id")).thenReturn(100.0);
//        
//        
//        EntityMovementGE entityMove = new EntityMovementGE(nodesFromAllGraphs,eventProcessor,entitySpeedStorage);
//        
//        Set<String> entityIds = new HashSet<String>();
//        entityIds.add("id");
//        
//        EntityPositionStorage entityPositionStorage = mock(EntityPositionStorage.class);
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(1));
//        
//        Map<String,Coordinate> map=  entityMove.getEntityPosition(entityIds, entityPositionStorage,regionBounds);
//        assertEquals(true, map.get("id").getLatitude() == node.getLatLon().lat());
//        
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(2));
//        when(eventProcessor.getCurrentTime()).thenReturn(new Long(1));
//        
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage,regionBounds);
//        assertEquals(true, map.get("id").getLatitude() == node.getLatLon().lat());
//        
//        when(eventProcessor.getCurrentTime()).thenReturn(new Long(20000));
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage,regionBounds);
//        assertEquals(true, map.get("id").getLatitude() != node1.getLatLon().lat());
//        
//        
//        when(eventProcessor.getCurrentTime()).thenReturn(new Long(20000000));
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage,regionBounds);
//        assertEquals(true, map.get("id").getLatitude() == node1.getLatLon().lat());
        
    }
    
//    @Test
//    public void test2(){
//        Node node = new Node(1, new LatLon(1.0, 1.0), null);
//        Node node1 = new Node(2, new LatLon(2.0, 2.0), null);
//        Map<Long, Node> nodesFromAllGraphs =  new HashMap<Long, Node>();
//        nodesFromAllGraphs.put(new Long(1), node);
//        nodesFromAllGraphs.put(new Long(2), node1);
//        
//        EntityMovementGE entityMove = new EntityMovementGE(nodesFromAllGraphs,2);
//        
//        Set<String> entityIds = new HashSet<String>();
//        entityIds.add("id");
//        
//        EntityPositionStorage entityPositionStorage = mock(EntityPositionStorage.class);
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(1));
//        
//        Map<String,Coordinate> map=  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        assertEquals(true,map.get("id").getLatitude() == node.getLatLon().lat());
//        
//        map =  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        assertEquals(true,map.get("id").getLatitude() == node.getLatLon().lat());
//        
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(2));
//        
//        map =  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        assertEquals(true,map.get("id").getLatitude() == node1.getLatLon().lat());
//    }
//    
//    @Test
//    public void test3(){
//        Node node = new Node(1, new LatLon(34.054761, -118.234254), null);
//        Node node1 = new Node(2, new LatLon(34.054439, -118.246734), null);
//        Map<Long, Node> nodesFromAllGraphs =  new HashMap<Long, Node>();
//        nodesFromAllGraphs.put(new Long(1), node);
//        nodesFromAllGraphs.put(new Long(2), node1);
//        
//        EntityMovementGE entityMove = new EntityMovementGE(nodesFromAllGraphs,800);
//        
//        Set<String> entityIds = new HashSet<String>();
//        entityIds.add("id");
//        
//        EntityPositionStorage entityPositionStorage = mock(EntityPositionStorage.class);
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(1));
//        
//        
//        Map<String,Coordinate> map=  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        assertEquals(1, map.size());
//        
//        when(entityPositionStorage.getEntityPositionByNodeId("id")).thenReturn(new Long(2));
//        
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        map=  entityMove.getEntityPosition(entityIds, entityPositionStorage);
//        assertEquals(true,map.get("id").getLatitude() == node1.getLatLon().lat());
//        
//    }
}
