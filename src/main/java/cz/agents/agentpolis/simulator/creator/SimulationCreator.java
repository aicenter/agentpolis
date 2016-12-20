package cz.agents.agentpolis.simulator.creator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.AgentStorage;
import cz.agents.agentpolis.simmodel.environment.model.Graphs;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.agentpolis.simulator.creator.initializator.MapInitFactory;
import cz.agents.agentpolis.simulator.creator.initializator.impl.MapData;
import cz.agents.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.agentpolis.simulator.visualization.visio.ProjectionProvider;
import cz.agents.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.agents.agentpolis.simulator.visualization.visio.entity.VisEntity;
import cz.agents.agentpolis.simulator.visualization.visio.entity.VisEntityLayer;
import cz.agents.agentpolis.simulator.visualization.visio.graph.VisGraph;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.LogItemViewer;
import cz.agents.agentpolis.utils.io.ResourceReader;
import cz.agents.agentpolis.utils.processing.post.CSVPostprocessingGroovyExecutor;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.EventProcessorEventType;
import cz.agents.alite.googleearth.updates.Synthetiser;
import cz.agents.alite.simulation.Simulation;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code SimulationCreator} initializes the simulation model according to added initializers including the
 * environment and agents.
 * The initialized simulation model is possible to run.
 */
@Singleton
public class SimulationCreator {

    /**
     * Logger for creator classes.
     */
    private static final Logger LOGGER = Logger.getLogger(SimulationCreator.class);
	
	public static SimulationCreator instance;
	
	public static void removeAgentStatic(Agent agent){
		instance.removeAgent(agent);
	}
	
	
	


    private Simulation simulation;

    private Synthetiser synthetiser;
    

    /**
     * write info about day to console
     */
    private boolean reportDays = false;

    private final List<SimulationFinishedListener> simulationFinishedListeners = new ArrayList<>();
    
    private final List<UpdateGEFactory> factoryGoogleEarths = new ArrayList<>();
    private final Map<EntityType, VisEntity> entityStyles = new HashMap<>();

    private final SimulationParameters params;
    
    private final LogItemViewer logItemViewer;
    
    private final ProjectionProvider projectionProvider;
    
    private final SimulationProvider simulationProvider;
    
    private final AllNetworkNodes allNetworkNodes;
    
    private final Graphs graphs;
    
    private final AgentStorage agentStorage;
    
    private final Provider<VisioInitializer> visioInitializerProvider;
    
    private final EntityVelocityModel entityVelocityModel;
    
    
    public BoundingBox boundsOfMap = null;
    
    private Projection projection;
	
	
	
	
	public Map<EntityType, VisEntity> getEntityStyles() {
		return entityStyles;
	}

    public BoundingBox getBoundsOfMap() {
        return boundsOfMap;
    }
    
    
	
	
	
	@Inject
    public SimulationCreator(final SimulationParameters params, LogItemViewer logItemViewer, 
            ProjectionProvider projectionProvider, SimulationProvider simulationProvider,
            AllNetworkNodes allNetworkNodes, Graphs graphs, AgentStorage agentStorage,
            Provider<VisioInitializer> visioInitializerProvider, EntityVelocityModel entityVelocityModel) {
        this.params = params;
        this.logItemViewer = logItemViewer;
        this.projectionProvider = projectionProvider;
        this.simulationProvider = simulationProvider;
        this.allNetworkNodes = allNetworkNodes;
        this.graphs = graphs;
        this.agentStorage = agentStorage;
        this.visioInitializerProvider = visioInitializerProvider;
        this.entityVelocityModel = entityVelocityModel;
		instance = this;
    }
    
    public void prepareSimulation(final MapInitFactory mapInitFactory, final long seed){
        LOGGER.debug("SEED = " + seed);
        
        initLogger();
        initSimulation();

        LOGGER.info(">>> MAPS CREATION");
        MapData osmDTO = mapInitFactory.initMap(params.osmFile, params.simulationDurationInMillis);
        boundsOfMap = osmDTO.bounds;

        initEnvironment(osmDTO, seed);

        // Projection
        projection = Projection.createGPSTo3DProjector(boundsOfMap, 10000, 10000);
        projectionProvider.setProjection(projection);

        if (reportDays) {
            new DayReporter(simulation);
        }
    }

    public void prepareSimulation(final MapInitFactory mapInitFactory) {
        prepareSimulation(mapInitFactory, 0L);
    }

    public void startSimulation() {
        long simTimeInit = System.currentTimeMillis();

        initVisioAndGE(projection);

        if (params.skipSimulation) {
            LOGGER.info("Skipping simulation...");
        } else {
            LOGGER.info(String.format("Simulation - init time: %s ms", (System.currentTimeMillis() - simTimeInit)));

            long simulationStartTime = System.currentTimeMillis();

            setUpTimeAndCompletenessEstimation();

            simulation.run();

            LOGGER.info(String.format("Simulation - runtime: %s ms", (System.currentTimeMillis() -
                    simulationStartTime)));

            if (params.turnOnGeneratingGELinks) {
                try {
                    synthetiser.stop();
                    LOGGER.info("Google Earth - Synthesizer was stopped ");
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }

            if (params.pathToScriptsAndTheirInputParameters != null) {
                LOGGER.info("Executing post groovy scripts:");
                (new CSVPostprocessingGroovyExecutor(params.pathToScriptsAndTheirInputParameters, params.pathToCSVEventLogFile,
                        params.dirForResults.getPath())).execute();
            }
        }
        simulationFinishedListeners.forEach(SimulationFinishedListener::simulationFinished);
    }

    // -------------------- Create methods
    // --------------------------------------------------


    private void initSimulation() {
        LOGGER.info("Setting up Alite simulation modul");
        simulation = new Simulation(params.simulationDurationInMillis);
        simulation.setSleepTimeIfWaitToOtherEvent(50);
        simulation.setPrintouts(10000000);
		simulationProvider.setSimulation(simulation);
        LOGGER.info("Set up Alite simulation modul");
    }

    private void initEnvironment(MapData osmDTO, long seed) {
        LOGGER.info("Creating instance of environment");
		allNetworkNodes.setAllNetworkNodes(osmDTO.nodesFromAllGraphs);
		graphs.setGraphs(osmDTO.graphByType);
        LOGGER.info("Created instance of environment");
    }

    private void initLogger() {
        LOGGER.info("Loading log4j properties");

        if (new File(params.LOG4J_XML_DIR).exists()) {
            try {
                DOMConfigurator.configure(params.LOG4J_XML_DIR);
                LOGGER.info("Loaded log4j properties");
                return;
            } catch (Exception ignored) {
                LOGGER.warn("Ignoring logger configuration exception.");
            }
        }

        URL pathToResource = ResourceReader.getPathToResource("/log4j/log4j.properties");
        if (pathToResource != null) {
            try {
                PropertyConfigurator.configure(pathToResource);
                LOGGER.info("Loaded log4j properties");
                return;
            } catch (Exception ignored) {
            }
        }

        pathToResource = ResourceReader.getPathToResource("/log4j/log4j.xml");

        if (pathToResource != null) {
            try {
                DOMConfigurator.configure(pathToResource);
                LOGGER.info("Loaded log4j properties");
                return;
            } catch (Exception ignored) {

            }
        }

        LOGGER.info("Failed to load log4j properties");
    }
	
	public void addAgent(Agent agent){
		addEntityMaxSpeedToStorage(agent.getId(), params.agentMoveSpeedInMps);
		agentStorage.addEntity(agent);
		if(params.showVisio && VisEntityLayer.isActive()){
			VisEntityLayer.addEntity(agent);
		}
		agent.born();
	}
	
	public void removeAgent(Agent agent){
		agent.die();
		
		if(params.showVisio  && VisEntityLayer.isActive()){
			VisEntityLayer.removeEntity(agent);
		}
        
		agentStorage.removeEntity(agent);
//		injector.getInstance(EntityVelocityModel.class).removeEntityMaxVelocity(agent.getId());
//        injector.getInstance(EntityPositionModel.class).removeEntity(agent.getId());
	}
    
    

    private void initVisioAndGE(Projection projection) {
        if (params.turnOnGeneratingGELinks) {
            LOGGER.info("Initializing Google Earth");
            createGoogleEarthUpdaters(params.pathToKMLFile);
            LOGGER.info("Initialized Google Earth");
        }

        if (params.showVisio) {
            LOGGER.info("Initializing Visio");
			visioInitializerProvider.get().initialize(simulation, projection);
            simulation.setSimulationSpeed(1);
            LOGGER.info("Initialized Visio");
        } 
        else {
            simulation.setSimulationSpeed(0);
        }

        if (params.showEventViewer) {
            LOGGER.info("Initializing event viewer");
            logItemViewer.runView();
            LOGGER.info("Initialized event viewer");
        }
    }

    // -------------------- Create methods
    // --------------------------------------------------

    private void addEntityMaxSpeedToStorage(String entityId, double agentMoveSpeedInkmph) {
        entityVelocityModel.addEntityMaxVelocity(entityId, agentMoveSpeedInkmph);
    }

    private void createGoogleEarthUpdaters(String nameOfKMLFile) {

        synthetiser = new Synthetiser();
        LOGGER.info("Google Earth - was started ");

        double north = boundsOfMap.getMaxLatE6() / 1E6;
        double east = boundsOfMap.getMaxLonE6() / 1E6;
        double south = boundsOfMap.getMinLatE6() / 1E6;
        double west = boundsOfMap.getMinLonE6() / 1E6;

        RegionBounds regionBounds = RegionBounds.createRegionBounds(north, south, east, west);

        for (UpdateGEFactory factoryGoogleEarth : factoryGoogleEarths) {
            
            // TODO - rework google earth to be able to work without injector
//            synthetiser.addUpdateKmlView(new CameraAltUpdateKmlProviderFacotryImpl(factoryGoogleEarth
//                    .getCameraAltVisibility(), factoryGoogleEarth.getNameUpdateKmlView(), factoryGoogleEarth
//                    .createUpdateKmlView(injector, regionBounds)));
        }

        synthetiser.makeLinks(new File(nameOfKMLFile));

        try {
            synthetiser.start();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

    }

    public void addEntityStyleVis(final EntityType entityType, Color colorOfEntityInVis, int widthOfEntityInVis) {
        entityStyles.put(entityType, new VisEntity(colorOfEntityInVis, widthOfEntityInVis));
    }

    private <TNode extends Node, TEdge extends Edge> VisGraph wrapGraph(Graph<TNode, TEdge> graph) {

        List<Node> nodes = new ArrayList<>(graph.getAllNodes());
        Map<Integer, Node> nodesWithIds = new HashMap<>();
        for (Node node : nodes) {
            nodesWithIds.put(node.id, node);
        }

        return new VisGraph(nodesWithIds, nodes, new ArrayList<>(graph.getAllEdges()));
    }

    /**
     * Adding Google Earth factory
     *
     * @param factoryGoogleEarth
     */
    public void addGoogleEarthUpdater(UpdateGEFactory factoryGoogleEarth) {
        factoryGoogleEarths.add(factoryGoogleEarth);
    }

    /**
     * reports some info every day
     */
    public class DayReporter implements EventHandler {

        private final Simulation sim;

        private long startTime;
        private long startEvents;
        private int counter;

        public DayReporter(Simulation sim) {
            this.sim = sim;
            counter = 0;
            System.currentTimeMillis();
            setTimer();
        }

        public String reportDay() {
            String ret = "";
            ret += "Day: " + counter + "\n";
            ret += "CPU time [s]: " + (System.currentTimeMillis() - startTime) / 1000 + "\n";
            ret += "Events: " + (sim.getEventCount() - startEvents) + "\n";
            return ret;
        }

        private void setTimer() {
            startTime = System.currentTimeMillis();
            startEvents = sim.getEventCount();
            counter++;

            sim.addEvent(this, Duration.ofHours(24).toMillis());
        }

		@Override
        public EventProcessor getEventProcessor() {
            return sim;
        }

		@Override
        public void handleEvent(Event event) {
            // System.exit(0);
            setTimer();
        }

    }

    public void addSimulationFinishedListener(SimulationFinishedListener simulationFinishedListener) {
        simulationFinishedListeners.add(simulationFinishedListener);
    }

    // -------------- cancel simulation --------------

    public void cancelSimulation() {
        simulation.addEvent(EventProcessorEventType.STOP, null, null, null);
    }

    // ----------------- estimation --------------------------------

    private void setUpTimeAndCompletenessEstimation() {
        long stepTimeForCompletenessEvent = (params.simulationDurationInMillis / 100);

        for (int percentValue = 1; percentValue < 100; percentValue++) {
            simulation.addEvent(new SimulationCompletenessHandler(), stepTimeForCompletenessEvent * percentValue);
        }

    }

    public long getSimulationCompletenessInPercentages() {
        return simulationCompletenessInPercentages;
    }

    private final static double PERCENTAGES_100 = 100.0;

    public long getTimeTillSimulationFinish() {
        if (simulationCompletenessInPercentages == 0) {
            return params.simulationDurationInMillis;
        }

        double durationPerPercentge = simulationDuration / (double) simulationCompletenessInPercentages;
        double remainPercentage = PERCENTAGES_100 - simulationCompletenessInPercentages;

        return (long) (remainPercentage * durationPerPercentge);
    }

    private long simulationCompletenessInPercentages = 0;
    private double simulationDuration = 0;

    private class SimulationCompletenessHandler implements EventHandler {

        @Override
        public void handleEvent(Event event) {
            simulationCompletenessInPercentages++;
            simulationDuration = System.currentTimeMillis() - params.simTimeInit;
        }

        @Override
        public EventProcessor getEventProcessor() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
