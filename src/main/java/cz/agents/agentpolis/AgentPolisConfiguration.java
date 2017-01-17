/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

/**
 *
 * @author fido
 */
public class AgentPolisConfiguration {

    public final boolean showEventViewer;
    public final String pathToCSVEventLogFile;
    public final int srid;
    public final long simulationDurationInMillis;
    public final File osmFile;
    public final boolean skipSimulation;
    public final boolean turnOnGeneratingGELinks;
    public final Map<String, String>  pathToScriptsAndTheirInputParameters;
    public final boolean showVisio;
    public final String pathToKMLFile;
    public final double agentMoveSpeedInMps;
    public final String LOG4J_XML_DIR;
    public final File dirForResults;

    public AgentPolisConfiguration(boolean showEventViewer, String pathToCSVEventLogFile, int srid, 
            long simulationDurationInMillis, File osmFile, boolean skipSimulation, boolean turnOnGeneratingGELinks, Map<String, String> pathToScriptsAndTheirInputParameters, boolean showVisio, String pathToKMLFile, double agentMoveSpeedInMps, String LOG4J_XML_DIR, File dirForResults) {
        this.showEventViewer = showEventViewer;
        this.pathToCSVEventLogFile = pathToCSVEventLogFile;
        this.srid = srid;
        this.simulationDurationInMillis = simulationDurationInMillis;
        this.osmFile = osmFile;
        this.skipSimulation = skipSimulation;
        this.turnOnGeneratingGELinks = turnOnGeneratingGELinks;
        this.pathToScriptsAndTheirInputParameters = pathToScriptsAndTheirInputParameters;
        this.showVisio = showVisio;
        this.pathToKMLFile = pathToKMLFile;
        this.agentMoveSpeedInMps = agentMoveSpeedInMps;
        this.LOG4J_XML_DIR = LOG4J_XML_DIR;
        this.dirForResults = dirForResults;
    }



    
    
    
}
