package cz.cvut.fel.aic.agentpolis.simulator.creator;

import cz.agents.agentpolis.utils.VelocityConverter;
import cz.agents.agentpolis.utils.config.ConfigReader;
import cz.agents.agentpolis.utils.config.ConfigReaderException;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Map;

public class SimulationParameters {

    /**
     * Names of various files needed by the simulation.
     */
    
    /**
     * Default map filename
     */
    public static final String CITY_DATA_MODEL_FILE_NAME = "citydatamodel.osm";
    public static final String PUBLIC_TRANSPORT_DATA_MODEL_FILE_NAME = "publictransportdatamodel.zip";
    public static final String POPULATION_DATA_MODEL_FILE_NAME = "populationdatamodel.xml";
    public static final String GTFS_ARCHIVE_FILE_NAME = "gtfs.zip";
    public static final String GTFS_UNPACKED_FOLDER_NAME = "GTFS/";


    private static Logger LOGGER = Logger.getLogger(SimulationParameters.class);

    public final ConfigReader configReader;
    public final File experimentPath;
    public final String pathToKMLFile;
    public final double agentMoveSpeedInMps;
    public final boolean showEventViewer;
    public final boolean showVisio;
    public final long simulationDurationInMillis;
    public final String pathToCSVEventLogFile;
    public final boolean turnOnGeneratingGELinks;
    public final Map<String, String> pathToScriptsAndTheirInputParameters;
    public final boolean skipSimulation;
    public final File dirForResults;
    public final long simTimeInit = 0;
    public final String LOG4J_XML_DIR;
    public final File osmFile;
    private int iterationNumber;
    public final String dataFolder;
    public final int srid;
    public final ZonedDateTime initDate;


    public SimulationParameters(File experimentPath, ConfigReader configReader) throws ConfigReaderException {
        this(experimentPath, configReader, 0);
    }

    public SimulationParameters(File experimentPath, ConfigReader configReader, int iterationNumber) 
            throws ConfigReaderException {
        this.configReader = configReader;
        this.experimentPath = experimentPath;
        this.iterationNumber = iterationNumber;
        this.dirForResults = initResultDir(experimentPath, iterationNumber);

        LOGGER.info("Loading configuration from config file");


        dataFolder = createDataFolder(experimentPath, configReader);
        LOG4J_XML_DIR = "log/log4j.xml";

        String osmFileName = dataFolder + configReader
                .getStringWithDefault("map", CITY_DATA_MODEL_FILE_NAME);

        LOGGER.info("Using OSM file '" + osmFileName + "'");
        osmFile = new File(osmFileName);
        validateOSMFile(osmFile);

        showEventViewer = configReader.getBoolean("showEventViewer");
        showVisio = configReader.getBoolean("showVisio");
        pathToKMLFile = dirForResults.getPath() + File.separator
                + configReader.getString("pathToKMLFile");
        agentMoveSpeedInMps = VelocityConverter.kmph2mps(configReader
                .getDouble("agentMoveSpeedInkmph"));
        simulationDurationInMillis = configReader.getLong("simulationDurationInMilisec");
        pathToCSVEventLogFile = dirForResults.getPath() + File.separator
                + configReader.getString("pathToCSVEventLogFile");
        turnOnGeneratingGELinks = configReader.getBoolean("turnOnGeneratingGELinks");
        skipSimulation = configReader.getBooleanWithDefault("skipSimulation", false);
        pathToScriptsAndTheirInputParameters = configReader
                .getMap("pathToScriptsAndTheirInputParameters");
        
        srid = configReader.getInteger("epsg");
        
        initDate = ZonedDateTime.now();

        LOGGER.info("Loaded configuration");
    }

    private void validateOSMFile(File osmFile) throws ConfigReaderException {
        if (!osmFile.exists()) {
            LOGGER.error("OSM file '" + osmFile.getAbsolutePath() + "' does not exist");
            throw new ConfigReaderException("osmFileName '" + osmFile.getAbsolutePath()
                    + "' does not refer to existing file");
        }
    }

    private String createDataFolder(File experimentPath, ConfigReader configReader) throws ConfigReaderException {
        String dataFolder = experimentPath + File.separator + configReader.getString("dataFolder");
        if (!dataFolder.endsWith(File.separator)) {
            dataFolder = dataFolder + File.separator;
        }
        return dataFolder;
    }


    private File initResultDir(File experimentDir, int iterationNumber) {
        String dirName = "results" + File.separator + iterationNumber;

        LOGGER.info("Creating result directory: " + dirName);

        File dirForResults = new File(experimentDir, dirName);
        if (!dirForResults.exists()) {
            dirForResults.mkdirs();
        }

        LOGGER.info("Created result directory: " + dirName);
        return dirForResults;
    }
}
