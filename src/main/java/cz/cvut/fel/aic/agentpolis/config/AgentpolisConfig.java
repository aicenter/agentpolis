package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class AgentpolisConfig implements GeneratedConfig {
  public String mapNodesFilepath;
  public Boolean showStackedEntities;


  public CongestionModel congestionModel;

  public String pathToSerializedGraph;

  public Integer simulationDurationInMillis;

  public Boolean showVisio;

  public String pathToMapTiles;

  public String osmTileServer;

  public String mapEdgesFilepath;

  public Boolean skipSimulation;

  public Integer srid;

  public AgentpolisConfig() {
  }

  public AgentpolisConfig fill(Map agentpolisConfig) {
    this.mapNodesFilepath = (String) agentpolisConfig.get("map_nodes_filepath");
    this.showStackedEntities = (Boolean) agentpolisConfig.get("show_stacked_entities");
    this.congestionModel = new CongestionModel((Map) agentpolisConfig.get("congestion_model"));
    this.pathToSerializedGraph = (String) agentpolisConfig.get("path_to_serialized_graph");
    this.simulationDurationInMillis = (Integer) agentpolisConfig.get("simulation_duration_in_millis");
    this.showVisio = (Boolean) agentpolisConfig.get("show_visio");
    this.pathToMapTiles = (String) agentpolisConfig.get("path_to_map_tiles");
    this.osmTileServer = (String) agentpolisConfig.get("osm_tile_server");
    this.mapEdgesFilepath = (String) agentpolisConfig.get("map_edges_filepath");
    this.skipSimulation = (Boolean) agentpolisConfig.get("skip_simulation");
    this.srid = (Integer) agentpolisConfig.get("srid");
    return this;
  }
}
