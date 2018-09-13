package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;

import ninja.fido.config.GeneratedConfig;

public class AgentpolisConfig implements GeneratedConfig {
    public Boolean showStackedEntities;

    public CongestionModel congestionModel;

    public ADModel adModel;

    public String log4jXmlDir;

    public Integer srid;

    public String pathToSaveRecordings;

    public String mapNodesFilepath;

    public String pathToSerializedGraph;

    public Integer simulationDurationInMillis;

    public Boolean showVisio;

    public String pathToMapTiles;

    public String osmTileServer;

    public String mapEdgesFilepath;

    public Boolean skipSimulation;

    public AgentpolisConfig() {
    }

    public AgentpolisConfig fill(Map agentpolisConfig) {
        this.showStackedEntities = (Boolean) agentpolisConfig.get("show_stacked_entities");
        this.congestionModel = new CongestionModel((Map) agentpolisConfig.get("congestion_model"));
        this.adModel = new ADModel((Map) agentpolisConfig.get("agentdrive"));
        this.log4jXmlDir = (String) agentpolisConfig.get("log4j_xml_dir");
        this.srid = (Integer) agentpolisConfig.get("srid");
        this.pathToSaveRecordings = (String) agentpolisConfig.get("path_to_save_recordings");
        this.mapNodesFilepath = (String) agentpolisConfig.get("map_nodes_filepath");
        this.pathToSerializedGraph = (String) agentpolisConfig.get("path_to_serialized_graph");
        this.simulationDurationInMillis = (Integer) agentpolisConfig.get("simulation_duration_in_millis");
        this.showVisio = (Boolean) agentpolisConfig.get("show_visio");
        this.pathToMapTiles = (String) agentpolisConfig.get("path_to_map_tiles");
        this.osmTileServer = (String) agentpolisConfig.get("osm_tile_server");
        this.mapEdgesFilepath = (String) agentpolisConfig.get("map_edges_filepath");
        this.skipSimulation = (Boolean) agentpolisConfig.get("skip_simulation");
        return this;
    }
}
