package cz.cvut.fel.aic.agentpolis.simulator.creator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeEventGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.Graphs;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandler;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.EventProcessorEventType;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import cz.agents.alite.googleearth.updates.Synthetiser;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.agentpolis.utils.ResourceReader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
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
	
	
	


    private TypedSimulation simulation;

    private Synthetiser synthetiser;
    

    /**
     * write info about day to console
     */
    private boolean reportDays = false;
    
    private final Config config;

    private final List<SimulationFinishedListener> simulationFinishedListeners = new ArrayList<>();
    
    private final List<UpdateGEFactory> factoryGoogleEarths = new ArrayList<>();
    
    private final SimulationProvider simulationProvider;
    
    private final AllNetworkNodes allNetworkNodes;
    
    private final Graphs graphs;
    
    private final Provider<VisioInitializer> visioInitializerProvider;
    
    private final TimeEventGenerator timeEventGenerator;

    
	
	
	
	@Inject
    public SimulationCreator(final Config config, SimulationProvider simulationProvider,
            AllNetworkNodes allNetworkNodes, Graphs graphs,
            Provider<VisioInitializer> visioInitializerProvider, TimeEventGenerator timeEventGenerator) {
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.allNetworkNodes = allNetworkNodes;
        this.graphs = graphs;
        this.visioInitializerProvider = visioInitializerProvider;
        this.timeEventGenerator = timeEventGenerator;
		instance = this;
    }
    
    public void prepareSimulation(final MapData osmDTO, final long seed){
        LOGGER.debug("SEED = " + seed);
        
        initLogger();
        initSimulation();

        LOGGER.info(">>> MAPS CREATION");

        initEnvironment(osmDTO, seed);

        if (reportDays) {
            new DayReporter(simulation);
        }
    }

    public void prepareSimulation(final MapData osmDTO) {
        prepareSimulation(osmDTO, 0L);
    }

    public void startSimulation() {
        long simTimeInit = System.currentTimeMillis();

        initVisioAndGE();

        if (config.skipSimulation) {
            LOGGER.info("Skipping simulation...");
        } else {
            LOGGER.info(String.format("Simulation - init time: %s ms", (System.currentTimeMillis() - simTimeInit)));

            long simulationStartTime = System.currentTimeMillis();

            setUpTimeAndCompletenessEstimation();
            
            timeEventGenerator.start();

            simulation.run();

            LOGGER.info(String.format("Simulation - runtime: %s ms", (System.currentTimeMillis() -
                    simulationStartTime)));

            if (config.turnOnGeneratingGeLinks) {
                try {
                    synthetiser.stop();
                    LOGGER.info("Google Earth - Synthesizer was stopped ");
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }

            if (!config.pathToScriptsAndTheirInputParameters.equals("")) {
                LOGGER.info("Executing post groovy scripts:");
                
                // legacy code - remove
//                (new CSVPostprocessingGroovyExecutor(config.pathToScriptsAndTheirInputParameters, 
//                        config.pathToCsvEventLogFile, config.dirForResults)).execute();
            }
        }
        simulationFinishedListeners.forEach(SimulationFinishedListener::simulationFinished);
    }

    // -------------------- Create methods
    // --------------------------------------------------


    private void initSimulation() {
        LOGGER.info("Setting up Alite simulation modul");
        simulation = new TypedSimulation(config.simulationDurationInMillis);
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

        if (new File(config.log4jXmlDir).exists()) {
            try {
                DOMConfigurator.configure(config.log4jXmlDir);
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
    

    private void initVisioAndGE() {
        if (config.turnOnGeneratingGeLinks) {
            LOGGER.info("Initializing Google Earth");
            createGoogleEarthUpdaters(config.pathToKmlFile);
            LOGGER.info("Initialized Google Earth");
        }

        if (config.showVisio) {
            LOGGER.info("Initializing Visio");
			visioInitializerProvider.get().initialize(simulation);
            simulation.setSimulationSpeed(1);
            LOGGER.info("Initialized Visio");
        } 
        else {
            simulation.setSimulationSpeed(0);
        }
    }

    // -------------------- Create methods
    // --------------------------------------------------

    private void createGoogleEarthUpdaters(String nameOfKMLFile) {

        synthetiser = new Synthetiser();
        LOGGER.info("Google Earth - was started ");

//        double north = boundsOfMap.getMaxLatE6() / 1E6;
//        double east = boundsOfMap.getMaxLonE6() / 1E6;
//        double south = boundsOfMap.getMinLatE6() / 1E6;
//        double west = boundsOfMap.getMinLonE6() / 1E6;

//        RegionBounds regionBounds = RegionBounds.createRegionBounds(north, south, east, west);

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
        long stepTimeForCompletenessEvent = (config.simulationDurationInMillis / 100);

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
            return config.simulationDurationInMillis;
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
            simulationDuration = System.currentTimeMillis();
        }

        @Override
        public EventProcessor getEventProcessor() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
