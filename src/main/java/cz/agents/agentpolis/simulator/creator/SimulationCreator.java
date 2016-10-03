package cz.agents.agentpolis.simulator.creator;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.siminfrastructure.logger.LoggerModul;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.EnvironmentFactory;
import cz.agents.agentpolis.simmodel.environment.model.*;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.*;
import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.agentpolis.simulator.creator.initializator.AgentInitFactory;
import cz.agents.agentpolis.simulator.creator.initializator.InitFactory;
import cz.agents.agentpolis.simulator.creator.initializator.InitModuleFactory;
import cz.agents.agentpolis.simulator.creator.initializator.MapInitFactory;
import cz.agents.agentpolis.simulator.creator.initializator.impl.MapData;
import cz.agents.agentpolis.simulator.logger.subscriber.CSVLogSubscriber;
import cz.agents.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.agentpolis.simulator.visualization.visio.SimulationControlLayer;
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
import cz.agents.alite.googleearth.cameraalt.factory.CameraAltUpdateKmlProviderFacotryImpl;
import cz.agents.alite.googleearth.updates.Synthetiser;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.VisManager;
import cz.agents.alite.vis.VisManager.SceneParams;
import cz.agents.alite.vis.layer.common.ColorLayer;
import cz.agents.alite.vis.layer.common.FpsLayer;
import cz.agents.alite.vis.layer.common.HelpLayer;
import cz.agents.alite.vis.layer.common.VisInfoLayer;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.List;

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
    private Injector injector = Guice.createInjector();
    private Synthetiser synthetiser;
    private LogItemViewer logItemViewer;

    /**
     * write info about day to console
     */
    private boolean reportDays = false;

    private final List<SimulationFinishedListener> simulationFinishedListeners = new ArrayList<>();
    private final List<AgentInitFactory> agentInits = new ArrayList<>();
    private final List<UpdateGEFactory> factoryGoogleEarths = new ArrayList<>();
    private final Map<EntityType, VisEntity> entityStyles = new HashMap<>();

    private final List<InitModuleFactory> initModuleFactories = new ArrayList<>();
    private final List<InitFactory> initFactories = new ArrayList<>();

    private final List<Object> loggers = new ArrayList<>();
    private final Set<Class<? extends LogItem>> allowedLogItemClassesForCSV = new HashSet<>();
    private final Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer = new HashSet<>();

    private final EnvironmentFactory factoryEnvironment;
    private final SimulationParameters params;
    public BoundingBox boundsOfMap = null;
	
	
	
	
	public Map<EntityType, VisEntity> getEntityStyles() {
		return entityStyles;
	}
	
	
	
	
    public SimulationCreator(final EnvironmentFactory factoryEnvironment, final SimulationParameters params) {
        this.factoryEnvironment = factoryEnvironment;
        this.params = params;
		instance = this;
    }
	
	public void setMainEnvironment(Injector injector){
		this.injector = injector;
	}

    public void startSimulation(final MapInitFactory mapInitFactory) {
        startSimulation(mapInitFactory, 0L);
    }

    public void startSimulation(final MapInitFactory mapInitFactory, final long seed) {
        LOGGER.debug("SEED = " + seed);
        long simTimeInit = System.currentTimeMillis();

        initLogger();
        initLoggers();
        initSimulation();

        LOGGER.info(">>> MAPS CREATION");
        MapData osmDTO = mapInitFactory.initMap(params.osmFile, injector, params.simulationDurationInMillis);
        boundsOfMap = osmDTO.bounds;

        initEnvironment(osmDTO, seed);

        // Projection
        Projection projection = Projection.createGPSTo3DProjector(boundsOfMap, 1000, 1000);

        initCSV(params.pathToCSVEventLogFile);
        if (params.showEventViewer) {
            logItemViewer = new LogItemViewer(allowedLogItemClassesLogItemViewer, injector.getInstance(TimeProvider
                    .class), params.simulationDurationInMillis);
            addLogger(logItemViewer);
        }

        for (InitModuleFactory initFactory : initModuleFactories) {
            LOGGER.debug("Injecting module: " + initFactory);
            AbstractModule module = initFactory.injectModule(injector);
            injector = injector.createChildInjector(module);
        }

        for (InitFactory initFactory : initFactories) {
            LOGGER.debug("Factory initialization: " + initFactory);
            initFactory.initRestEnvironment(injector);
        }

        initAgents();

        if (reportDays) {
            new DayReporter(simulation);
        }

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
		injector.getInstance(SimulationProvider.class).setSimulation(simulation);
        LOGGER.info("Set up Alite simulation modul");

    }

    private void initEnvironment(MapData osmDTO, long seed) {
        LOGGER.info("Creating instance of environment");
		injector.getInstance(AllNetworkNodes.class).setAllNetworkNodes(osmDTO.nodesFromAllGraphs);
		injector.getInstance(Graphs.class).setGraphs(osmDTO.graphByType);
        injector = factoryEnvironment.injectEnvironment(injector, simulation, seed, osmDTO.graphByType, osmDTO
                .nodesFromAllGraphs);
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

    private void initAgents() {
        LOGGER.info("Initializing agents");
        for (AgentInitFactory agentInit : agentInits) {
            List<Agent> initedAgents = agentInit.initAllAgentLifeCycles(injector);
            for (Agent agent : initedAgents) {
                //                LOGGER.debug("Agent initialized: " + agent + " (move speed: " + agentMoveSpeedInMps
                //                        + " m/s)");
                addEntityMaxSpeedToStorage(agent.getId(), params.agentMoveSpeedInMps);
                injector.getInstance(AgentStorage.class).addEntity(agent);
                agent.born();
            }
        }
        LOGGER.info("Initialized agents");
    }
	
	public void addAgent(Agent agent){
		addEntityMaxSpeedToStorage(agent.getId(), params.agentMoveSpeedInMps);
		injector.getInstance(AgentStorage.class).addEntity(agent);
		if(params.showVisio){
			VisEntityLayer.addEntity(agent);
		}
		agent.born();
	}
	
	public void removeAgent(Agent agent){
		agent.die();
		
		if(params.showVisio){
			VisEntityLayer.removeEntity(agent);
		}
		injector.getInstance(AgentStorage.class).removeEntity(agent);
		injector.getInstance(EntityVelocityModel.class).removeEntityMaxVelocity(agent.getId());
	}

    private void initLoggers() {
        LOGGER.info("Initialization of logger - event bus");
        injector = injector.createChildInjector(new LoggerModul(loggers));
    }

    private void initVisioAndGE(Projection projection) {
        if (params.turnOnGeneratingGELinks) {
            LOGGER.info("Initializing Google Earth");
            createGoogleEarthUpdaters(params.pathToKMLFile);
            LOGGER.info("Initialized Google Earth");
        }

        if (params.showVisio) {
            LOGGER.info("Initializing Visio");
            visFirst(projection);
			injector.getInstance(VisioInitializer.class).initialize(simulation, projection);
            visLast();

            simulation.setSimulationSpeed(1);
            LOGGER.info("Initialized Visio");
        } else {
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
        injector.getInstance(EntityVelocityModel.class).addEntityMaxVelocity(entityId, agentMoveSpeedInkmph);

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
            synthetiser.addUpdateKmlView(new CameraAltUpdateKmlProviderFacotryImpl(factoryGoogleEarth
                    .getCameraAltVisibility(), factoryGoogleEarth.getNameUpdateKmlView(), factoryGoogleEarth
                    .createUpdateKmlView(injector, regionBounds)));
        }

        synthetiser.makeLinks(new File(nameOfKMLFile));

        try {
            synthetiser.start();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

    }

    /**
     * creates the alite vis window
     */
    private void visFirst(final Projection projection) {

        final int windowHight = 400;
        final int windowWidth = 400;

        VisManager.setInitParam("Agentpolis operator", windowWidth, windowHight);
        VisManager.init();

        final double zoomFactor = windowWidth / projection.sceneWidth;

        VisManager.setSceneParam(new SceneParams() {

            @Override
            public double getDefaultZoomFactor() {
                return zoomFactor;
            }

            @Override
            public Rectangle getWorldBounds() {
                return new Rectangle(projection.sceneWidth, projection.sceneHeight);
            }

        });
        VisManager.registerLayer(ColorLayer.create(Color.LIGHT_GRAY));
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
     * creates upper layers
     */
    protected void visLast() {
        VisManager.registerLayer(HelpLayer.create());
        VisManager.registerLayer(FpsLayer.create());
        VisManager.registerLayer(VisInfoLayer.create());
        // VisManager.registerLayer(LogoLayer.create(ResourceReader.getPathToResource("/img/atg_blue.png")));

        VisManager.registerLayer(SimulationControlLayer.create(simulation, injector));
    }

    /**
     * Adding init agent.
     *
     * @param agentInit
     */
    public void addAgentInit(AgentInitFactory agentInit) {
        agentInits.add(agentInit);

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

    /**
     * Add your own logger which processes the incoming the implementations of {@code LogItem}. These
     * implementations of
     * {@code LogItem} should be subscribed by logger via {@code Subscribe} annotation
     *
     * @param logger
     */
    public void addLogger(Object logger) {

        if (isSubscribeAnnotationIncluded(logger)) {
            loggers.add(logger);
        } else {
            LOGGER.info("The logger [" + logger + "] was skipped because it does not cointains Subscribe annotation");
        }

    }

    private boolean isSubscribeAnnotationIncluded(Object logger) {
        for (Method method : logger.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                return true;
            }

        }
        return false;
    }

    public void addAllowedLogItemForCSVLogger(Class<? extends LogItem> allowedLogItemClass) {
        allowedLogItemClassesForCSV.add(allowedLogItemClass);
    }

    public void addAllowedLogItemForCSVLogger(Set<Class<? extends LogItem>> allowedLogItemClasses) {
        allowedLogItemClassesForCSV.addAll(allowedLogItemClasses);
    }

    public void addAllowEventForEventViewer(Class<? extends LogItem> allowedLogItemClass) {
        this.allowedLogItemClassesLogItemViewer.add(allowedLogItemClass);
    }

    public void addAllowEventForEventViewer(Set<Class<? extends LogItem>> allowedLogItemClasses) {
        this.allowedLogItemClassesLogItemViewer.addAll(allowedLogItemClasses);
    }

    /**
     * @param initFactory
     * @deprecated Please use addInitModuleFactory (same functionality, only fixing a name).
     */
    @Deprecated
    public void addInitModulFactory(InitModuleFactory initFactory) {
        initModuleFactories.add(initFactory);
    }

    public void addInitModuleFactory(InitModuleFactory initFactory) {
        initModuleFactories.add(initFactory);
    }

    public void addInitFactory(InitFactory initFactory) {
        initFactories.add(initFactory);
    }

    public void addSimulationFinishedListener(SimulationFinishedListener simulationFinishedListener) {
        simulationFinishedListeners.add(simulationFinishedListener);
    }

    private void initCSV(String pathToCSVEventLogFile) {

        try {
            if (!allowedLogItemClassesForCSV.isEmpty()) {
                CSVLogSubscriber csvLogSubscriber = CSVLogSubscriber.newInstance(ImmutableSet.copyOf
                        (allowedLogItemClassesForCSV), new File(pathToCSVEventLogFile));
                addLogger(csvLogSubscriber);
                addSimulationFinishedListener(csvLogSubscriber);
                return;
            }
        } catch (IOException e) {
            LOGGER.warn("CSV Logger was not initialized", e);
        }
        LOGGER.warn("CSV Logger was not initialized");
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
