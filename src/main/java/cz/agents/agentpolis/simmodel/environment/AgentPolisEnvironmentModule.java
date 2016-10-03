package cz.agents.agentpolis.simmodel.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import java.time.ZonedDateTime;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.agent.activity.movement.callback.PassengerActivityCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.callback.VehicleArrivedCallback;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MovingActionCallback;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
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
import cz.agents.agentpolis.simmodel.environment.model.sensor.UsingPublicTransportActivityCallback;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

/**
 * Implementation of Guice module, which support creation of follow models including in AgentPolis environment
 * <p>
 * <ul> <li>{@code UsingPassengerTransportModel}</li> <li>{@code PublicTransportModel}</li> <li>{@code
 * VehicleTimeModel}</li> <li>{@code BeforePlanNotifyModel}</li> <li>{@code VehiclePlanNotificationModel}</li>
 * <li>{@code VehicleGroupModel}</li> <li>{@code DelayModel}</li> <li>{@code VehicleGroupModel}</li> <li>{@code
 * LinkedEntityModel}</li> <li>{@code VehiclePositionModel}</li> <li>{@code AgentPositionModel}</li> <li>{@code
 * AgentStorage}</li> <li>{@code VehicleStorage}</li> <li>{@code EntityVelocityModel}</li> <li>{@code
 * PublicTransportModel}</li> </ul>
 *
 * @author Zbynek Moler
 */
public class AgentPolisEnvironmentModule extends AbstractModule {

	private final EventProcessor eventProcessor;
	private final Random random;
	private final Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs;
	private final DelayingSegmentCapacityDeterminer delayingSegmentCapacityDeterminer;
	private final ZonedDateTime initDate;

	public AgentPolisEnvironmentModule(EventProcessor eventProcessor, Random random,
									   Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs,
									   Map<Integer, SimulationNode> nodesFromAllGraphs, ZonedDateTime initDate) {
		super();
		this.eventProcessor = eventProcessor;
		this.random = random;
		this.graphs = graphs;
		this.initDate = initDate;
		this.delayingSegmentCapacityDeterminer = new DefaultDelayingSegmentCapacityDeterminer();
	}

	public AgentPolisEnvironmentModule(EventProcessor eventProcessor, Random random,
									   Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs,
									   Map<Integer, SimulationNode> nodesFromAllGraphs,
									   DelayingSegmentCapacityDeterminer delayingSegmentCapacityDeterminer,
									   ZonedDateTime initDate) {
		super();
		this.eventProcessor = eventProcessor;
		this.random = random;
		this.graphs = graphs;
		this.delayingSegmentCapacityDeterminer = delayingSegmentCapacityDeterminer;
		this.initDate = initDate;
	}

	@Override
	protected void configure() {

		bind(new TypeLiteral<Map<String, PassengerActivityCallback<?>>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, UsingPublicTransportActivityCallback>>() {
		}).toInstance(new HashMap<>());

		bind(new TypeLiteral<Map<String, PassengerBeforePlanNotifySensor>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, Set<String>>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, String>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, MovingActionCallback>>() {
		}).toInstance(new HashMap<>());

		bind(new TypeLiteral<Map<VehicleAndPositionKey, Set<String>>>() {
		}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, VehicleArrivedCallback>>() {
		}).toInstance(new HashMap<>());

	}

	@Provides
	@Singleton
	Random provideRandom() {
		return random;
	}

	@Provides
	@Singleton
	DelayModel provideDelayHandler() {
		return new DelayModelImpl(createQueueStorage(), eventProcessor, new JunctionHandlerImpl());
	}

	@Provides
	@Singleton
	TimeProvider provideTimeProvider() {
		return new TimeProvider(eventProcessor, initDate);
	}

	private Map<GraphTypeAndToNodeKey, Map<GraphTypeAndFromToNodeKey, DelayingSegment>> createQueueStorage() {

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

	private static class DefaultDelayingSegmentCapacityDeterminer implements DelayingSegmentCapacityDeterminer {

		@Override
		public double determineDelaySegmentCapacity(double maxCapacity) {
			return maxCapacity;
		}

	}
}
