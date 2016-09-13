package cz.agents.agentpolis.siminfrastructure.planner.multimodal;


public class StationTimeTableTest {

//    @Test
//    public void stationTimeTableTest(){        
//        
//        Duration arriveTime = new Duration(82560000);
//        
//        StationTimeTable stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(0)));        
//        assertEquals(0,stationTimeTable.getNearestLeaveTime(arriveTime).getMillis());
//        
//        
//        stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
//        assertEquals(1000,stationTimeTable.getNearestLeaveTime(new Duration(10)).getMillis());
//        
//        stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
//        assertEquals(86401000,stationTimeTable.getNearestLeaveTime(new Duration(90000000)).getMillis());
//    }
//    
//    @Test
//    public void stationTimeTableTest2(){        
//        
//        Duration arriveTime = new Duration(1);
//        
//        StationTimeTable stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
//        assertEquals(1000, stationTimeTable.getNextLeaveTime(1));
//        assertEquals(1000, stationTimeTable.getNextLeaveTime(arriveTime).getMillis());
//        assertEquals(1000, stationTimeTable.getNextLeaveTime(new ZonedDateTime(arriveTime.getMillis())).getMillis());
//    }
//    
//    @Test
//    public void stationTimeTableTest3(){        
//        
//        Duration arriveTime = new Duration(5000);
//        
//        StationTimeTable stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
//        assertEquals(86401000, stationTimeTable.getNextLeaveTime(5000));
//        assertEquals(86401000, stationTimeTable.getNextLeaveTime(arriveTime).getMillis());
//        assertEquals(86401000, stationTimeTable.getNextLeaveTime(new ZonedDateTime(arriveTime.getMillis())).getMillis());
//    }
//    
////    @Test
////    public void stationTimeTableTest4(){
////        
////        
////        StationTimeTable stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(0)));        
////        assertEquals(0,stationTimeTable.getNearestLeaveTime(82560000).getMillis());
////        
////        
////        stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
////        assertEquals(1000,stationTimeTable.getNearestLeaveTime(10).getMillis());
////        
////        stationTimeTable = new StationTimeTable(Arrays.asList(new Duration(1000)));        
////        assertEquals(86401000,stationTimeTable.getNearestLeaveTime(90000000).getMillis());
////        
////    }
  
}
