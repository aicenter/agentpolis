package cz.agents.agentpolis.agentpolis.config;

import java.lang.Double;
import java.lang.Integer;
import java.util.HashMap;
import ninja.fido.config.GeneratedConfig;

public class Config implements GeneratedConfig {
  public Integer connectionTickLength;

  public Integer batchSize;

  public Double maxFowPerLane;

  public Config() {
  }

  public Config fill(HashMap config) {
    this.connectionTickLength = (Integer) config.get("connection_tick_length");
    this.batchSize = (Integer) config.get("batch_size");
    this.maxFowPerLane = (Double) config.get("max_fow_per_lane");
    return this;
  }
}
