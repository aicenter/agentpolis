package cz.agents.agentpolis.simmodel.environment.model;


public class StatisticsStorageTest {

//    private StatisticsStorage statisticsStorage;
//    
//    @Before
//    public void setUp(){
//        
//        ZonedDateTime ZonedDateTime = new ZonedDateTime(0);
//        statisticsStorage = new StatisticsStorage(null, ZonedDateTime);
//    }
//    
//    
//    
//    private enum EStatsEnum implements IStatsEnum{
//        STAT1,
//        STAT2;
//
//        public EnumType getEnumType() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        public int getOrdinal() {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        public String getUniqueStatsName() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        public IStatsEnum[] statsValues() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//        
//    }
//    
//    @Test
//    public void statsTest1(){
//        statisticsStorage.addEnum(1, "owner", "message", EStatsEnum.STAT1, null, RecordType.ALL);
//        statisticsStorage.addEnum(2, "owner", "message2", EStatsEnum.STAT2, null, RecordType.ALL);
//        statisticsStorage.addMessage(3, "owner", "message", null, RecordType.ALL);
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecords();
//        assertEquals("message", abstractList.get(0).message);
//        assertEquals("message2", abstractList.get(1).message);
//        assertEquals(RecordType.ALL, abstractList.get(1).type);
//        assertEquals(EStatsEnum.STAT2, ((RecordEnum)abstractList.get(1)).enm);
//        assertEquals(3, abstractList.size());
//    }
// 
//    @Test
//    public void statsTest2(){
//        statisticsStorage.addMessage(1, "owner", "message", null, RecordType.ALL);
//        statisticsStorage.addMessage(2, "owner2", "message2", null, RecordType.ALL);
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecords();
//        assertEquals("message", abstractList.get(0).message);
//        assertEquals("message2", abstractList.get(1).message);
//        assertEquals(2, abstractList.size());
//    } 
//    
//    @Test
//    public void statsTest3(){
//        statisticsStorage.addNumber(1, "owner", "message", 1.0, null, RecordType.ALL);
//        statisticsStorage.addNumber(2, "owner2", "message2", 1.0, null, RecordType.ALL);
//
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecords();
//        assertEquals("message", abstractList.get(0).message);
//        assertEquals("message2", abstractList.get(1).message);
//        assertEquals(1.0, ((RecordNum)abstractList.get(1)).num,1);
//        assertEquals(2, abstractList.size());
//    }
//    
//    @Test
//    public void statsTest4(){
//        statisticsStorage.addNumbers(1, "owner", "message", new double[]{1.0,2.0}, null, RecordType.ALL);
//                
//        AbstractList<Record> abstractList = statisticsStorage.getRecords();
//        assertEquals("message", abstractList.get(0).message);
//        assertEquals(2.0, ((RecordNums)abstractList.get(0)).nums[1],1);
//        assertEquals(1, abstractList.size());
//    }
//
//    @Test
//    public void statsTest5(){
//        statisticsStorage.addNumber(1, "owner", "message", 1.0, null, RecordType.ALL);
//        statisticsStorage.addNumber(2, "owner2", "message2", 1.0, null, RecordType.ALL);
//
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecords();
//        assertEquals(2, abstractList.size());
//        
//        abstractList = statisticsStorage.getRecords(RecordType.ALL);
//        assertEquals(2, abstractList.size());
//        
//        abstractList = statisticsStorage.getRecords(RecordType.BODY_GET_CAR);
//        assertEquals(0, abstractList.size());
//    }
//    
//    @Test
//    public void statsTest6(){
//        statisticsStorage.addNumber(1, "owner", "message", 1.0, null, RecordType.ALL);
//        statisticsStorage.addNumber(2, "owner2", "message2", 1.0, null, RecordType.ALL);
//
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecordsByOwner("owner");
//        assertEquals(1, abstractList.size());
//        
//        abstractList = statisticsStorage.getRecordsByOwner("owner3");
//        assertEquals(0, abstractList.size());
//    }
//    
//    @Test
//    public void statsTest7(){
//        statisticsStorage.addNumber(1, "owner", "message", 1.0, null, RecordType.ALL);
//        statisticsStorage.addNumber(2, "owner2", "message2", 1.0, null, RecordType.ALL);
//
//        
//        AbstractList<Record> abstractList = statisticsStorage.getRecords("owner", RecordType.ALL);
//        assertEquals(1, abstractList.size());
//        
//        abstractList = statisticsStorage.getRecords("owner3", RecordType.ALL);
//        assertEquals(0, abstractList.size());
//    }
}
