package cz.agents.agentpolis.agentpolis.config;

import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import ninja.fido.config.GeneratedConfig;

public class Config implements GeneratedConfig {
  public Boolean showEventViewer;

  public String pathToScriptsAndTheirInputParameters;

  public Integer batchSize;

  public Double maxFowPerLane;

  public String pathToKmlFile;

  public Boolean turnOnGeneratingGeLinks;

  public String log4jXmlDir;

  public Integer srid;

  public String dirForResults;

  public Integer connectionTickLength;

  public Integer simulationDurationInMillis;

  public Boolean showVisio;

  public String pathToCsvEventLogFile;

  public Boolean skipSimulation;

  public Config() {
  }

  public Config fill(HashMap config) {
    this.showEventViewer = (Boolean) config.get("show_event_viewer");
    this.pathToScriptsAndTheirInputParameters = (String) config.get("path_to_scripts_and_their_input_parameters");
    this.batchSize = (Integer) config.get("batch_size");
    this.maxFowPerLane = (Double) config.get("max_fow_per_lane");
    this.pathToKmlFile = (String) config.get("path_to_kml_file");
    this.turnOnGeneratingGeLinks = (Boolean) config.get("turn_on_generating_ge_links");
    this.log4jXmlDir = (String) config.get("log4j_xml_dir");
    this.srid = (Integer) config.get("srid");
    this.dirForResults = (String) config.get("dir_for_results");
    this.connectionTickLength = (Integer) config.get("connection_tick_length");
    this.simulationDurationInMillis = (Integer) config.get("simulation_duration_in_millis");
    this.showVisio = (Boolean) config.get("show_visio");
    this.pathToCsvEventLogFile = (String) config.get("path_to_csv_event_log_file");
    this.skipSimulation = (Boolean) config.get("skip_simulation");
    return this;
  }
}
