/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner.ShortestPathPlannerFactory;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.Graphs;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.BikewayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.HighwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.MetrowayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.PedestrianNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.RailwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TramwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayModel;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayingSegment;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.factory.DelayingSegmentCapacityDeterminer;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.factory.DelayingSegmentFactory;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelImpl;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.JunctionHandlerImpl;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndFromToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.GraphTypeAndToNodeKey;
import cz.agents.agentpolis.simmodel.environment.model.key.VehicleAndPositionKey;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PassengerBeforePlanNotifySensor;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.simmodel.environment.model.sensor.UsingPublicTransportActivityCallback;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.agentpolis.simulator.creator.SimulationParameters;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.agentpolis.simulator.visualization.visio.ProjectionProvider;
import cz.agents.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.agents.agentpolis.simulator.visualization.visio.DefaultVisioInitializer;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.LogItemViewer;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author F-I-D-O
 */
public class StandardAgentPolisModule extends AbstractModule implements AgentPolisMainModule{
    
    private final DefaultDelayingSegmentCapacityDeterminer delayingSegmentCapacityDeterminer;
    

	private SimulationParameters parameters;

    private List<Object> loggers;
    
    private Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer;
    
    
	
	
	public StandardAgentPolisModule() {
        this.delayingSegmentCapacityDeterminer = new DefaultDelayingSegmentCapacityDeterminer();
	}

	
	

	@Override
	protected void configure() {
        
        bindConstant().annotatedWith(Names.named("mapSrid")).to(parameters.srid);
		
		// bindings for storages
		bind(new TypeLiteral<Map<String, Agent>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, Vehicle>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, AgentPolisEntity>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<EntityType, Set<String>>>(){}).toInstance(new HashMap<>());
		
		// bindings for position models
		bind(new TypeLiteral<Map<String, Set<PositionUpdated>>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<KeyWithString, Set<PositionUpdated>>>(){}).toInstance(new HashMap<>());
        
        //binding sfor VehicleNotificationModel
        bind(new TypeLiteral<Map<VehicleAndPositionKey, Set<String>>>(){}).toInstance(new HashMap<>());
        bind(new TypeLiteral<Map<String, VehicleArrivedCallback>>(){}).toInstance(new HashMap<>());
        
        // bindings for BeforePlanNotifyModel
        bind(new TypeLiteral<Map<String, MovingActionCallback>>(){}).toInstance(new HashMap<>());
        bind(new TypeLiteral<Map<String, PassengerBeforePlanNotifySensor>>(){}).toInstance(new HashMap<>());
        bind(new TypeLiteral<Map<String, String>>(){}).toInstance(new HashMap<>());
        bind(new TypeLiteral<Map<String, Set<String>>>(){}).toInstance(new HashMap<>());
        
        // bindings for UsingPassengerTransportModel
        bind(new TypeLiteral<Map<String, PassengerActivityCallback<?>>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, UsingPublicTransportActivityCallback>>() {}).toInstance(new HashMap<>());
        
        bind(SimulationParameters.class).toInstance(parameters);
        
        install(new FactoryModuleBuilder().implement(ShortestPathPlanner.class, ShortestPathPlanner.class)
            .build(ShortestPathPlannerFactory.class));
        
		bindVisioInitializer();
		configureNext();
	}

	protected void configureNext() {
		
	}
	
	
	@Provides 
	@Singleton
	public EventProcessor getEventProcessor(SimulationProvider simulationProvider){
		return simulationProvider.getSimulation();
	}
    
    @Provides 
	@Singleton
	public Projection getProjection(ProjectionProvider projectionProvider){
		return projectionProvider.getProjection();
	}
	
	@Provides
	@Singleton
	MetrowayNetwork provideMetrowayNetwork(Graphs graphs) {
		return new MetrowayNetwork(graphs.getGraphs().get(EGraphType.METROWAY));
	}

	@Provides
	@Singleton
	BikewayNetwork provideBikewayNetwork(Graphs graphs) {
		return new BikewayNetwork(graphs.getGraphs().get(EGraphType.BIKEWAY));
	}

	@Provides
	@Singleton
	TramwayNetwork provideTramwayNetwork(Graphs graphs) {
		return new TramwayNetwork(graphs.getGraphs().get(EGraphType.TRAMWAY));
	}

	@Provides
	@Singleton
	RailwayNetwork provideRailwayNetwork(Graphs graphs) {
		return new RailwayNetwork(graphs.getGraphs().get(EGraphType.RAILWAY));
	}

	@Provides
	@Singleton
	HighwayNetwork provideHighwayNetwork(Graphs graphs) {
		return new HighwayNetwork(graphs.getGraphs().get(EGraphType.HIGHWAY));
	}

	@Provides
	@Singleton
	PedestrianNetwork providePedestrianNetwork(Graphs graphs) {
		return new PedestrianNetwork(graphs.getGraphs().get(EGraphType.PEDESTRIAN));
	}

	@Provides
	@Singleton
	TransportNetworks provideTransportNetworks(Graphs graphs) {
		return new TransportNetworks(graphs.getGraphs());
	}
    
    @Provides
	@Singleton
	DelayModel provideDelayHandler(EventProcessor eventProcessor, Graphs graphs) {
		return new DelayModelImpl(createQueueStorage(graphs.getGraphs()), eventProcessor, new JunctionHandlerImpl());
	}

    @Provides
    @Singleton
    PublishSubscribeLogger providePublishSubscribeLogger() {
        EventBus eventBus = new EventBus();
        for (Object publishSubscribeLogger : loggers) {
            eventBus.register(publishSubscribeLogger);
        }
        return new PublishSubscribeLogger(eventBus);
    }
    
    @Provides
    @Singleton
    LogItemViewer provideLogItemViewer(Provider<TimeProvider> timeProvider) {
        return new LogItemViewer(allowedLogItemClassesLogItemViewer, timeProvider, 
                parameters.simulationDurationInMillis);
    }

	@Provides
	@Singleton
	TimeProvider provideTimeProvider(EventProcessor eventProcessor) {
		return new TimeProvider(eventProcessor, parameters.initDate);
	}
    
    

	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(DefaultVisioInitializer.class);
	}
	
    
    private Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> 
        createQueueStorage(Map<GraphType,Graph<SimulationNode, SimulationEdge>> graphs) {

		DelayingSegmentFactory queueItemsFactory = new DelayingSegmentFactory();

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

					if (length <= 0) {
						length = 1;
					}

					length = delayingSegmentCapacityDeterminer.determineDelaySegmentCapacity(length);

					innerQueues.put(graphTypeAndFromToNodeKey, queueItemsFactory.createDelayingSegmentInstance
							(length));
					queues.put(graphTypeAndToNodeKey, innerQueues);
				}
			}
		}
		return queues;
	}

    @Override
    public void initializeParametrs(SimulationParameters parameters, List<Object> loggers, 
            Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer) {
		this.parameters = parameters;
        this.loggers = loggers;
        this.allowedLogItemClassesLogItemViewer = allowedLogItemClassesLogItemViewer;
    }
        
    private static class DefaultDelayingSegmentCapacityDeterminer implements DelayingSegmentCapacityDeterminer {

		@Override
		public double determineDelaySegmentCapacity(double maxCapacity) {
			return maxCapacity;
		}

	}
}
