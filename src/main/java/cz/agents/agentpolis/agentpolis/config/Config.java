package cz.agents.agentpolis.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import ninja.fido.config.GeneratedConfig;

public class Config implements GeneratedConfig {
  public Boolean showEventViewer;

  public String pathToScriptsAndTheirInputParameters;

  public String dirForResults;

  public String pathToKmlFile;

  public CongestionModel congestionModel;

  public Integer simulationDurationInMillis;

  public Boolean turnOnGeneratingGeLinks;

  public Boolean showVisio;

  public String log4jXmlDir;

  public String pathToCsvEventLogFile;

  public Boolean skipSimulation;

  public Integer srid;

  public Config() {
  }

  public Config fill(HashMap config) {
    this.showEventViewer = (Boolean) config.get("show_event_viewer");
    this.pathToScriptsAndTheirInputParameters = (String) config.get("path_to_scripts_and_their_input_parameters");
    this.dirForResults = (String) config.get("dir_for_results");
    this.pathToKmlFile = (String) config.get("path_to_kml_file");
    this.congestionModel = new CongestionModel((HashMap) config.get("congestion_model"));
    this.simulationDurationInMillis = (Integer) config.get("simulation_duration_in_millis");
    this.turnOnGeneratingGeLinks = (Boolean) config.get("turn_on_generating_ge_links");
    this.showVisio = (Boolean) config.get("show_visio");
    this.log4jXmlDir = (String) config.get("log4j_xml_dir");
    this.pathToCsvEventLogFile = (String) config.get("path_to_csv_event_log_file");
    this.skipSimulation = (Boolean) config.get("skip_simulation");
    this.srid = (Integer) config.get("srid");
    return this;
  }
}
