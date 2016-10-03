/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.Graphs;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.BikewayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.HighwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.MetrowayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.PedestrianNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.RailwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TramwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.agentpolis.simulator.creator.SimulationParameters;
import cz.agents.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.agents.agentpolis.simulator.visualization.visio.entity.DefaultVisioInitializer;
import cz.agents.agentpolis.utils.key.KeyWithString;
import cz.agents.alite.common.event.EventProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author F-I-D-O
 */
public class StandardAgentPolisModule extends AbstractModule{
	
	private final EnvironmentFactory envinromentFactory;
	
	private final SimulationParameters parameters;
	
	
	
	
	public StandardAgentPolisModule(EnvironmentFactory envinromentFactory, SimulationParameters parameters) {
		this.envinromentFactory = envinromentFactory;
		this.parameters = parameters;
	}

	
	

	@Override
	protected void configure() {
		
		// bindings for storages
		bind(new TypeLiteral<Map<String, Agent>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, Vehicle>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<EntityStorage<AgentPolisEntity>>(){});
		bind(new TypeLiteral<Map<String, AgentPolisEntity>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<EntityType, Set<String>>>(){}).toInstance(new HashMap<>());
		
		// bindings for position models
		bind(new TypeLiteral<Map<String, Integer>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<String, Set<PositionUpdated>>>(){}).toInstance(new HashMap<>());
		bind(new TypeLiteral<Map<KeyWithString, Set<PositionUpdated>>>(){}).toInstance(new HashMap<>());
		
		bindVisioInitializer();
		configureNext();
	}

	private void configureNext() {
		
	}
	
	@Provides 
	@Singleton
	public SimulationCreator getSimulationCreator(){
		return new SimulationCreator(envinromentFactory, parameters);
	}
	
	@Provides 
	@Singleton
	public EventProcessor getEventProcessor(SimulationProvider simulationProvider){
		return simulationProvider.getSimulation();
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

	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(DefaultVisioInitializer.class);
	}
	
}
