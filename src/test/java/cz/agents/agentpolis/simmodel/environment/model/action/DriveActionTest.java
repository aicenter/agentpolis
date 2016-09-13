//package cz.agents.agentpolis.simmodel.environment.model.action;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import org.joda.time.Duration;
//import org.junit.Before;
//import org.junit.Test;
//
//import cz.agents.agentpolis.mock.Mocks;
//import cz.agents.agentpolis.mock.graph.GraphMock;
//import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.DrivingActivityCallback;
//import cz.agents.agentpolis.simmodel.environment.model.SpeedLimitModel;
//import cz.agents.agentpolis.simmodel.environment.model.action.driving.DriveAction;
//import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
//import cz.agents.agentpolis.simmodel.environment.model.key.GraphFromToNodeKey;
//import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.action.LinkEntityAction;
//import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.sensor.LinkedEntitySensor;
//import cz.agents.agentpolis.simmodel.environment.model.publictransport.TimeTableItem;
//import cz.agents.alite.common.event.EventHandler;
//
//public class DriveActionTest {
//
//    Mocks mocks;
//
//    @Before
//    public void setUp() {
//        mocks = new Mocks();
//    }
//
//    @Test
//    public void driveActionTest() {
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.run();
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node2.getId(), positionOfDriver);
//        assertEquals(GraphMock.node2.getId(), positionOfVehicle);
//
//    }
//
//    @Test
//    public void driveActionAndSeedInfluenceTest() {
//
//        Map<GraphFromToNodeKey, Double> speedLimistForSpecificRoadSegments = new HashMap<SpeedLimitModel.GraphFromToNodeKey, Double>();
//        speedLimistForSpecificRoadSegments.put(new GraphFromToNodeKey(
//                GraphMock.EGraphTypeMock.GRAPH1, GraphMock.node1.getId(), GraphMock.node2.getId()),
//                (double) 20);
//
//        SpeedLimitModel speedLimitModel = new SpeedLimitModel(speedLimistForSpecificRoadSegments);
//
//        mocks.speedInfluenceModels.addSpeedInfluenceModel(speedLimitModel);
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//        mocks.entityVelocityModel.addEntityMaxVelocity(mocks.vehicle.getId(), new Double(40));
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.run();
//        mocks.eventProcessor.setRunning(true);
//
//        assertEquals(mocks.eventProcessor.getCurrentTime(),
//                MoveUtil.computeDuration(20, GraphMock.edge12.getLenght()) + 1);
//
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node2.getId(), GraphMock.node3.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        long orgTime = mocks.eventProcessor.getCurrentTime();
//        mocks.eventProcessor.run();
//
//        assertEquals(mocks.eventProcessor.getCurrentTime() - orgTime,
//                MoveUtil.computeDuration(40, GraphMock.edge23.getLenght()) + 1);
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node3.getId(), positionOfDriver);
//        assertEquals(GraphMock.node3.getId(), positionOfVehicle);
//
//    }
//
//    @Test
//    public void driveActionWithPassengerTest() {
//
//        LinkedEntitySensor linkedEntityCallback = mock(LinkedEntitySensor.class);
//        mocks.linkedEntityModel.linkEnities(mocks.vehicle.getId(), mocks.passenger.getId(),
//                linkedEntityCallback);
//
//        driveActionTest();
//
//        long positionOfPassenger = mocks.agentPositionModel
//                .getEntityPositionByNodeId(mocks.passenger.getId());
//        assertEquals(GraphMock.node2.getId(), positionOfPassenger);
//
//    }
//
//    @Test
//    public void driveActionFinishedTest() {
//
//        LinkedEntitySensor linkedEntityCallback = mock(LinkedEntitySensor.class);
//        mocks.linkedEntityModel.linkEnities(mocks.vehicle.getId(), mocks.passenger.getId(),
//                linkedEntityCallback);
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.run();
//
//        Set<String> linkedEntities = mocks.linkedEntityModel
//                .getLinkedEntites(mocks.vehicle.getId());
//        assertEquals(2, linkedEntities.size());
//
//        driveAction.finishedDriving(mocks.vehicle.getId(), mock(DrivingActivityCallback.class));
//
//        mocks.eventProcessor.setRunning(true);
//        mocks.eventProcessor.run();
//
//        linkedEntities = mocks.linkedEntityModel.getLinkedEntites(mocks.vehicle.getId());
//        assertEquals(0, linkedEntities.size());
//
//    }
//
//    @Test
//    public void driveWithDepartureTimeActionTest() {
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        mocks.vehicleTimeModel.addVehicleDepartureDayFlag(mocks.vehicle.getId(), 0);
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.run();
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node2.getId(), positionOfDriver);
//        assertEquals(GraphMock.node2.getId(), positionOfVehicle);
//        assertEquals(72496, mocks.eventProcessor.getCurrentTime());
//
//    }
//
//    @Test
//    public void driveWithDepartureTimeActionTest2() {
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityVelocityModel.addEntityMaxVelocity(mocks.vehicle.getId(), 20.0);
//
//        mocks.vehicleTimeModel.addVehicleDepartureDayFlag(mocks.vehicle.getId(), 0);
//        TimeTableItem tableItem = new TimeTableItem(Duration.standardMinutes(30).getMillis(), false);
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.run();
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node2.getId(), positionOfDriver);
//        assertEquals(GraphMock.node2.getId(), positionOfVehicle);
//        assertEquals(171451, mocks.eventProcessor.getCurrentTime());
//
//    }
//
//    @Test
//    public void driveWithDepartureTimeActionTest3() {
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityVelocityModel.addEntityMaxVelocity(mocks.vehicle.getId(), 20.0);
//
//        TimeTableItem tableItem = new TimeTableItem(Duration.standardMinutes(30).getMillis(), false);
//
//        mocks.vehicleTimeModel.addVehicleDepartureDayFlag(mocks.vehicle.getId(), 0);
//        mocks.eventProcessor.addEvent(mock(EventHandler.class), Duration.standardMinutes(45)
//                .getMillis());
//        mocks.eventProcessor.run();
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.setRunning(true);
//        mocks.eventProcessor.run();
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node2.getId(), positionOfDriver);
//        assertEquals(GraphMock.node2.getId(), positionOfVehicle);
//        assertEquals(2871451, mocks.eventProcessor.getCurrentTime());
//
//    }
//
//    @Test
//    public void driveWithDepartureTimeActionTest4() {
//
//        LinkEntityAction linkEntityAction = mocks.linkEntityAction;
//        linkEntityAction.linkEnities(mocks.vehicle.getId(), mocks.driver.getId());
//
//        mocks.entityVelocityModel.addEntityMaxVelocity(mocks.vehicle.getId(), 20.0);
//
//        TimeTableItem tableItem = new TimeTableItem(Duration.standardHours(1).getMillis(), true);
//
//        mocks.vehicleTimeModel.addVehicleDepartureDayFlag(mocks.vehicle.getId(), 0);
//        mocks.eventProcessor.addEvent(mock(EventHandler.class), Duration.standardMinutes(23)
//                .getMillis());
//        mocks.eventProcessor.run();
//
//        mocks.entityPositionVPS.put(mocks.vehicle.getId(), GraphMock.node1.getId());
//
//        DriveAction driveAction = mocks.driveAction;
//        driveAction.driven(mocks.vehicle.getId(), GraphMock.node1.getId(), GraphMock.node2.getId(),
//                GraphMock.EGraphTypeMock.GRAPH1);
//
//        mocks.eventProcessor.setRunning(true);
//        mocks.eventProcessor.run();
//
//        long positionOfDriver = mocks.agentPositionModel.getEntityPositionByNodeId(mocks.driver
//                .getId());
//        long positionOfVehicle = mocks.vehiclePositionModel.getEntityPositionByNodeId(mocks.vehicle
//                .getId());
//
//        assertEquals(GraphMock.node2.getId(), positionOfDriver);
//        assertEquals(GraphMock.node2.getId(), positionOfVehicle);
//        assertEquals(1551451, mocks.eventProcessor.getCurrentTime());
//
//    }
// }
