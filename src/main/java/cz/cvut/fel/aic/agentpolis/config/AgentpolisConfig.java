package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class AgentpolisConfig implements GeneratedConfig {
  public Boolean debug;

  public CongestionModel congestionModel;

  public String log4jXmlDir;

  public Integer srid;

  public String pathToSaveRecordings;

  public Integer maxVehicleSpeedInMeters;

  public Boolean useMeasuredSpeed;

  public Visio visio;

  public String mapNodesFilepath;

  public SimulationDuration simulationDuration;

  public String pathToSerializedGraph;

  public String pathToMapTiles;

  public String osmTileServer;

  public String mapEdgesFilepath;

  public Boolean skipSimulation;

  public AgentpolisConfig() {
  }

  public AgentpolisConfig fill(Map agentpolisConfig) {
    this.debug = (Boolean) agentpolisConfig.get("debug");
    this.congestionModel = new CongestionModel((Map) agentpolisConfig.get("congestion_model"));
    this.log4jXmlDir = (String) agentpolisConfig.get("log4j_xml_dir");
    this.srid = (Integer) agentpolisConfig.get("srid");
    this.pathToSaveRecordings = (String) agentpolisConfig.get("path_to_save_recordings");
    this.maxVehicleSpeedInMeters = (Integer) agentpolisConfig.get("max_vehicle_speed_in_meters");
    this.useMeasuredSpeed = (Boolean) agentpolisConfig.get("use_measured_speed");
    this.visio = new Visio((Map) agentpolisConfig.get("visio"));
    this.mapNodesFilepath = (String) agentpolisConfig.get("map_nodes_filepath");
    this.simulationDuration = new SimulationDuration((Map) agentpolisConfig.get("simulation_duration"));
    this.pathToSerializedGraph = (String) agentpolisConfig.get("path_to_serialized_graph");
    this.pathToMapTiles = (String) agentpolisConfig.get("path_to_map_tiles");
    this.osmTileServer = (String) agentpolisConfig.get("osm_tile_server");
    this.mapEdgesFilepath = (String) agentpolisConfig.get("map_edges_filepath");
    this.skipSimulation = (Boolean) agentpolisConfig.get("skip_simulation");
    return this;
  }
}
