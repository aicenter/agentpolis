package cz.agents.agentpolis.mock;

import com.google.inject.*;
import cz.agents.agentpolis.mock.graph.GraphMock;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.activity.TimeSpendingActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.DriveVehicleActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.RideInVehicleActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.RideOnPTActivity;
import cz.agents.agentpolis.simmodel.agent.activity.movement.WalkActivity;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.*;
import cz.agents.agentpolis.simmodel.environment.model.action.AgentPositionAction;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerAction;
import cz.agents.agentpolis.simmodel.environment.model.action.PassengerTripAction;
import cz.agents.agentpolis.simmodel.environment.model.action.VehiclePositionAction;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.MoveVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveEntityAction;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.passenger.PassengerVehiclePlanAction;
import cz.agents.agentpolis.simmodel.environment.model.action.passenger.WaitForVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehicleBeforePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehiclePlanNotifyAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.VehicleTimeAction;
import cz.agents.agentpolis.simmodel.environment.model.action.vehicle.WaitingVehicleAction;
import cz.agents.agentpolis.simmodel.environment.model.action.walking.WalkingAction;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayModel;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayingSegment;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.action.DelayAction;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.factory.DelayingSegmentFactory;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelImpl;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.JunctionHandlerImpl;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.query.AgentInfoQuery;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.query.VehicleInfoQuery;
import cz.agents.agentpolis.simmodel.environment.model.key.VehicleAndPositionKey;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.LinkedEntityModel;
import cz.agents.agentpolis.simmodel.environment.model.linkedentitymodel.action.LinkEntityAction;
import cz.agents.agentpolis.simmodel.environment.model.query.AgentPositionQuery;
import cz.agents.agentpolis.simmodel.environment.model.query.VehiclePositionQuery;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PassengerBeforePlanNotifySensor;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.simmodel.environment.model.speed.SpeedInfluenceModels;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocks extends AbstractModule {

	public Agent walkerAgent;
	public Agent driver;
	public Agent driver2;
	public Vehicle vehicle;
	public Vehicle groupVehicle1;
	public Vehicle groupVehicle2;
	public Agent passenger;
	public Agent passenger2;

	public static final String GROUP_ID = "GroupId";

	public EventProcessor eventProcessor;

	public VehicleGroupModel vehicleGroupModel;
	public EntityVelocityModel entityVelocityModel;
	public AgentPositionModel agentPositionModel;
	public VehiclePositionModel vehiclePositionModel;
	public LinkedEntityModel linkedEntityModel;
	public VehicleTimeModel vehicleTimeModel;
	public UsingPassengerTransportModel usingPassengerTransportModel;
	public VehiclePlanNotificationModel vehiclePlanNotificationModel;
	public BeforePlanNotifyModel beforePlanNotifyModel;

	public TimeProvider timeProvider;

	private AgentStorage agents;
	private VehicleStorage vehicles;

	public final Map<String, Integer> entityPositionAPS;
	public final Map<String, Set<PositionUpdated>> positionCallbacksAPS;
	public final Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPositionAPS;
	public final Map<String, Integer> entityPositionVPS;
	public final Map<String, Set<PositionUpdated>> positionCallbacksVPS;
	public final Map<KeyWithString, Set<PositionUpdated>> callbackBoundedWithPositionVPS;

	public final Map<VehicleAndPositionKey, Set<String>> waitingPassengersOnSpecificPosition = new HashMap<>();
	public final Map<String, VehicleArrivedCallback> passengerAndVehiclePlanCallback = new HashMap<>();

	public final Map<String, PassengerBeforePlanNotifySensor> passengerBeforePlanNotifySensorCallbackBPNS = new HashMap<>();
	public final Map<String, Set<String>> currentlyInformPassengersBeforePlanNotifyBPNS = new HashMap<>();
	public final Map<String, String> passengerIdLinkedWithVehicleIdBPNS = new HashMap<>();
	public final Map<String, MovingActionCallback> movingActionCallbackBPNS = new HashMap<>();

	public final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByType;

	public MoveEntityAction moveToNextNodeAction;
	public LinkEntityAction linkEntityAction;
	public AgentPositionQuery agentPositionSensor;
	public AgentPositionAction agentPositionAction;
	public VehiclePositionAction vehiclePositionAction;
	public VehiclePositionQuery vehiclePositionSensor;
	public WalkingAction walkingAction;
	public WaitForVehicleAction passengerWaitingAction;
	public PassengerVehiclePlanAction passengerVehiclePlanAction;
	public PassengerAction useVehicleAction;
	public AgentInfoQuery agentInfoSensor;
	public PassengerTripAction passengerTripAction;
	public DelayAction queueAction;
	public MoveVehicleAction driveAction;
	public VehicleInfoQuery vehicleSensor;
	public VehicleTimeAction timeAction;
	public VehiclePlanNotifyAction vehiclePlanNotifyAction;
	public WaitingVehicleAction waitingVehicleAction;
	public VehicleBeforePlanNotifyAction vehicleBeforePlanNotifyAction;

	public SpeedInfluenceModels speedInfluenceModels;

	public TimeSpendingActivity timeSpendingActivity;
	public DriveVehicleActivity vehicleDrivingActivity;
	public WalkActivity walkingActivity;
	public RideOnPTActivity passengerActivityPTTrip;
	public RideInVehicleActivity passengerActivityVehicleTrip;

	public DelayModel delayHandler;

	public Random random;

	public Injector injector;

	public Mocks() {

		random = new Random(0);

		eventProcessor = new EventProcessor();

		walkerAgent = mock(Agent.class);
		when(walkerAgent.getId()).thenReturn("walkerId");

		driver = mock(Agent.class);
		when(driver.getId()).thenReturn("driverId");

		driver2 = mock(Agent.class);
		when(driver2.getId()).thenReturn("driverId2");

		vehicle = mock(Vehicle.class);
		when(vehicle.getId()).thenReturn("vehicleId");
		when(vehicle.getCapacity()).thenReturn(2);

		groupVehicle1 = mock(Vehicle.class);
		when(groupVehicle1.getId()).thenReturn("groupVehicle1");
		when(groupVehicle1.getCapacity()).thenReturn(2);

		groupVehicle2 = mock(Vehicle.class);
		when(groupVehicle2.getId()).thenReturn("groupVehicle2");
		when(groupVehicle2.getCapacity()).thenReturn(2);

		passenger = mock(Agent.class);
		when(passenger.getId()).thenReturn("passengerId");

		passenger2 = mock(Agent.class);
		when(passenger2.getId()).thenReturn("passenger2Id");

		entityVelocityModel = new EntityVelocityModel();
		entityVelocityModel.addEntityMaxVelocity(walkerAgent.getId(), 4.3);
		entityVelocityModel.addEntityMaxVelocity(driver.getId(), 4.3);
		entityVelocityModel.addEntityMaxVelocity(driver2.getId(), 4.3);
		entityVelocityModel.addEntityMaxVelocity(vehicle.getId(), 47.3);
		entityVelocityModel.addEntityMaxVelocity(groupVehicle1.getId(), 47.3);
		entityVelocityModel.addEntityMaxVelocity(groupVehicle2.getId(), 47.3);

		entityPositionAPS = new HashMap<>();
		positionCallbacksAPS = new HashMap<>();
		callbackBoundedWithPositionAPS = new HashMap<>();

		agentPositionModel = new AgentPositionModel(positionCallbacksAPS,
                callbackBoundedWithPositionAPS, eventProcessor);

		entityPositionVPS = new HashMap<>();
		positionCallbacksVPS = new HashMap<>();
		callbackBoundedWithPositionVPS = new HashMap<>();

		vehiclePositionModel = new VehiclePositionModel(positionCallbacksVPS,
                callbackBoundedWithPositionVPS, eventProcessor);

		linkedEntityModel = new LinkedEntityModel();

		vehicleGroupModel = new VehicleGroupModel();
		vehicleGroupModel.addVehicleToGroup(GROUP_ID, groupVehicle1.getId());
		vehicleGroupModel.addVehicleToGroup(GROUP_ID, groupVehicle2.getId());

		vehicleTimeModel = new VehicleTimeModel();

		agents = new AgentStorage();

		vehicles = new VehicleStorage();

		beforePlanNotifyModel = new BeforePlanNotifyModel(new HashMap<>(), new HashMap<>(), new HashMap<>(), new
                HashMap<>(), eventProcessor);

		usingPassengerTransportModel = new UsingPassengerTransportModel(new HashMap<>(), new HashMap<>(),
                eventProcessor);

		vehiclePlanNotificationModel = new VehiclePlanNotificationModel(waitingPassengersOnSpecificPosition,
                passengerAndVehiclePlanCallback, eventProcessor);

		graphByType = GraphMock.graphByType;
		// graphByType.put(GraphMock.EGraphTypeMock.GRAPH1, GraphMock.graph);
		// graphByType.put(EGraphType.PEDESTRIAN, GraphMock.pedestrianGraph);
		// graphByType.put(EGraphType.HIGHWAY, GraphMock.streetGraph);

		timeProvider = new TimeProvider(eventProcessor);

		delayHandler = new DelayModelImpl(createQueueStorage(), eventProcessor, new JunctionHandlerImpl());

		injector = Guice.createInjector(this);

		timeSpendingActivity = injector.getInstance(TimeSpendingActivity.class);
		vehicleDrivingActivity = injector.getInstance(DriveVehicleActivity.class);
		walkingActivity = injector.getInstance(WalkActivity.class);
		passengerActivityPTTrip = injector.getInstance(RideOnPTActivity.class);
		passengerActivityVehicleTrip = injector.getInstance(RideInVehicleActivity.class);

		speedInfluenceModels = injector.getInstance(SpeedInfluenceModels.class);

		addActionAndSensor(walkerAgent, agents);
		addActionAndSensor(driver, agents);
		addActionAndSensor(driver2, agents);
		addActionAndSensor(vehicle, vehicles);
		addActionAndSensor(groupVehicle1, vehicles);
		addActionAndSensor(groupVehicle2, vehicles);
		addActionAndSensor(passenger, agents);
		addActionAndSensor(passenger2, agents);

	}

	public <T extends AgentPolisEntity> void addActionAndSensor(T agentPolisEntity, EntityStorage<T> entityStorage) {

		moveToNextNodeAction = injector.getInstance(MoveEntityAction.class);
		linkEntityAction = injector.getInstance(LinkEntityAction.class);
		agentPositionSensor = injector.getInstance(AgentPositionQuery.class);
		agentPositionAction = injector.getInstance(AgentPositionAction.class);
		walkingAction = injector.getInstance(WalkingAction.class);
		passengerWaitingAction = injector.getInstance(WaitForVehicleAction.class);
		passengerVehiclePlanAction = injector.getInstance(PassengerVehiclePlanAction.class);
		useVehicleAction = injector.getInstance(PassengerAction.class);
		agentInfoSensor = injector.getInstance(AgentInfoQuery.class);
		passengerTripAction = injector.getInstance(PassengerTripAction.class);
		queueAction = injector.getInstance(DelayAction.class);
		driveAction = injector.getInstance(MoveVehicleAction.class);
		vehicleSensor = injector.getInstance(VehicleInfoQuery.class);
		timeAction = injector.getInstance(VehicleTimeAction.class);
		vehiclePlanNotifyAction = injector.getInstance(VehiclePlanNotifyAction.class);
		waitingVehicleAction = injector.getInstance(WaitingVehicleAction.class);
		vehicleBeforePlanNotifyAction = injector.getInstance(VehicleBeforePlanNotifyAction.class);
		vehiclePositionAction = injector.getInstance(VehiclePositionAction.class);
		vehiclePositionSensor = injector.getInstance(VehiclePositionQuery.class);

		entityStorage.addEntity(agentPolisEntity);
	}

	private Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> createQueueStorage() {

		DelayingSegmentFactory queueItemsFactory = new DelayingSegmentFactory();

		Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = graphByType;

		Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> queues = new HashMap<>();

		for (GraphType graphType : graphs.keySet()) {
			Graph<?, ?> graph = graphs.get(graphType);
			for (Node fromNode : graph.getAllNodes()) {
				int fromNodeById = fromNode.id;
				for (Edge toNodeWithEdge : graph.getOutEdges(fromNodeById)) {
					Integer toNodeById = toNodeWithEdge.toId;

					GraphTypeAndToNodeKey graphTypeAndToNodeKey = new GraphTypeAndToNodeKey(graphType, toNodeById);

					Map<GraphTypeAndFromToNodeKey, DelayingSegment> innerQueues = queues.get(graphTypeAndToNodeKey);
					if (innerQueues == null) {
						innerQueues = new HashMap<>();
					}

					GraphTypeAndFromToNodeKey graphTypeAndFromToNodeKey = new GraphTypeAndFromToNodeKey(graphType,
                            fromNodeById, toNodeById);
					double length = toNodeWithEdge.length;

					innerQueues.put(graphTypeAndFromToNodeKey, queueItemsFactory.createDelayingSegmentInstance
                            (length));
					queues.put(graphTypeAndToNodeKey, innerQueues);

				}
			}

		}

		return queues;
	}

	// -------------------------------------------------------------------------------

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}

	@Provides
	@Singleton
	AllNetworkNodes provideAllNetworkNodes() {
		return new AllNetworkNodes(new HashMap<>());
	}

	@Provides
	@Singleton
	TransportNetworks provideTransportNetworks() {
		return new TransportNetworks(graphByType);
	}

	@Provides
	@Singleton
	DelayModel provideDelayHandler() {
		return delayHandler;
	}

	@Provides
	@Singleton
	UsingPassengerTransportModel provideUsingPassengerTransportModel() {
		return usingPassengerTransportModel;
	}

	@Provides
	@Singleton
	AgentStorage provideAgentStorage() {
		return agents;
	}

	@Provides
	@Singleton
	VehicleStorage provideVehicleStorage() {
		return vehicles;
	}

	@Provides
	@Singleton
	EventProcessor provideEventProcessor() {
		return eventProcessor;
	}

	@Provides
	@Singleton
	VehiclePlanNotificationModel provideVehiclePlanNotificationModel() {
		return vehiclePlanNotificationModel;
	}

	@Provides
	@Singleton
	BeforePlanNotifyModel provideBeforePlanNotifyModel() {
		return beforePlanNotifyModel;
	}

	@Provides
	@Singleton
	Random provideRandom() {
		return random;
	}

	@Provides
	@Singleton
	AgentPositionModel provideAgentPositionModel() {
		return agentPositionModel;
	}

	@Provides
	@Singleton
	VehiclePositionModel provideVehiclePositionModel() {
		return vehiclePositionModel;
	}

	@Provides
	@Singleton
	VehicleGroupModel provideVehicleGroupModel() {
		return vehicleGroupModel;
	}

	@Provides
	@Singleton
	EntityVelocityModel provideEntityVelocityModel() {
		return entityVelocityModel;
	}

	@Provides
	@Singleton
	LinkedEntityModel provideLinkedEntityModel() {
		return linkedEntityModel;
	}

	@Provides
	@Singleton
	VehicleTimeModel provideVehicleTimeModel() {
		return vehicleTimeModel;
	}

	@Provides
	@Singleton
	TimeProvider provideTimeProvider() {
		return timeProvider;
	}

}
