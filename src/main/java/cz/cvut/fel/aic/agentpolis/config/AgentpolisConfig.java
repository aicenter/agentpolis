package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class AgentpolisConfig implements GeneratedConfig {
  public Boolean showEventViewer;

  public String pathToScriptsAndTheirInputParameters;

  public String dirForResults;

  public CongestionModel congestionModel;

  public Integer simulationDurationInMillis;

  public Boolean showVisio;

  public String pathToMapTiles;

  public String osmTileServer;

  public String log4jXmlDir;

  public String pathToCsvEventLogFile;

  public Boolean skipSimulation;

  public Integer srid;

  public AgentpolisConfig() {
  }

  public AgentpolisConfig fill(Map agentpolisConfig) {
    this.showEventViewer = (Boolean) agentpolisConfig.get("show_event_viewer");
    this.pathToScriptsAndTheirInputParameters = (String) agentpolisConfig.get("path_to_scripts_and_their_input_parameters");
    this.dirForResults = (String) agentpolisConfig.get("dir_for_results");
    this.congestionModel = new CongestionModel((Map) agentpolisConfig.get("congestion_model"));
    this.simulationDurationInMillis = (Integer) agentpolisConfig.get("simulation_duration_in_millis");
    this.showVisio = (Boolean) agentpolisConfig.get("show_visio");
    this.pathToMapTiles = (String) agentpolisConfig.get("path_to_map_tiles");
    this.osmTileServer = (String) agentpolisConfig.get("osm_tile_server");
    this.log4jXmlDir = (String) agentpolisConfig.get("log4j_xml_dir");
    this.pathToCsvEventLogFile = (String) agentpolisConfig.get("path_to_csv_event_log_file");
    this.skipSimulation = (Boolean) agentpolisConfig.get("skip_simulation");
    this.srid = (Integer) agentpolisConfig.get("srid");
    return this;
  }
}
