/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.system;

import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner.ShortestPathPlannerFactory;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.DefaultVisioInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.alite.simulation.Simulation;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;
import cz.cvut.fel.aic.geographtools.util.Utils2D;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.logging.Level;
import ninja.fido.config.Configuration;

/**
 *
 * @author F-I-D-O
 */
public class StandardAgentPolisModule extends AbstractModule implements AgentPolisMainModule{
    
    protected final Config config;
    

    private List<Object> loggers;
    

    public Config getConfig() {
        return config;
    }
    
    
	
	
	public StandardAgentPolisModule() {
        this.config = Configuration.load(new Config());
        Log.init("AgentPolis logger", Level.FINE, "log.txt");
	}

	
	

	@Override
	protected void configure() {
        
        bindConstant().annotatedWith(Names.named("mapSrid")).to(config.srid);
        
        bind(Config.class).toInstance(config);
        
        bind(TimeProvider.class).to(StandardTimeProvider.class);
        
        install(new FactoryModuleBuilder().implement(ShortestPathPlanner.class, ShortestPathPlanner.class)
            .build(ShortestPathPlannerFactory.class));
        
		bindVisioInitializer();
		configureNext();
	}

	protected void configureNext() {
		
	}
    
    
    @Provides 
	@Singleton
	public GraphSpec2D getMapSpecification(HighwayNetwork highwayNetwork){
		return Utils2D.getGraphSpec(highwayNetwork.getNetwork());
	}
    
	@Provides 
	@Singleton
	public Simulation getSimulation(SimulationProvider simulationProvider){
		return simulationProvider.getSimulation();
	}
	
	@Provides 
	@Singleton
	public EventProcessor getEventProcessor(SimulationProvider simulationProvider){
		return simulationProvider.getSimulation();
	}
    
    @Provides 
	@Singleton
	public TypedSimulation getTypedSimulation(SimulationProvider simulationProvider){
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

	@Provides
	@Singleton
	StandardTimeProvider provideTimeProvider(EventProcessor eventProcessor) {
		return new StandardTimeProvider(eventProcessor, ZonedDateTime.now());
	}
    
    

	protected void bindVisioInitializer() {
		bind(VisioInitializer.class).to(DefaultVisioInitializer.class);
	}
}
