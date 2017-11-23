/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class AgentpolisConfig implements GeneratedConfig {
  public CongestionModel congestionModel;

  public Integer simulationDurationInMillis;

  public Boolean showVisio;

  public String pathToMapTiles;

  public String osmTileServer;

  public String log4jXmlDir;

  public Boolean skipSimulation;

  public Integer srid;

  public AgentpolisConfig() {
  }

  public AgentpolisConfig fill(Map agentpolisConfig) {
    this.congestionModel = new CongestionModel((Map) agentpolisConfig.get("congestion_model"));
    this.simulationDurationInMillis = (Integer) agentpolisConfig.get("simulation_duration_in_millis");
    this.showVisio = (Boolean) agentpolisConfig.get("show_visio");
    this.pathToMapTiles = (String) agentpolisConfig.get("path_to_map_tiles");
    this.osmTileServer = (String) agentpolisConfig.get("osm_tile_server");
    this.log4jXmlDir = (String) agentpolisConfig.get("log4j_xml_dir");
    this.skipSimulation = (Boolean) agentpolisConfig.get("skip_simulation");
    this.srid = (Integer) agentpolisConfig.get("srid");
    return this;
  }
}
