package cz.agents.agentpolis.agentpolis.config;

import java.lang.Double;
import java.lang.Integer;
import java.util.HashMap;

public class CongestionModel {
  public Integer randomSeed;

  public Integer connectionTickLength;

  public Double maxFlowPerLane;

  public Integer batchSize;

  public CongestionModel(HashMap congestionModel) {
    this.randomSeed = (Integer) congestionModel.get("random_seed");
    this.connectionTickLength = (Integer) congestionModel.get("connection_tick_length");
    this.maxFlowPerLane = (Double) congestionModel.get("max_flow_per_lane");
    this.batchSize = (Integer) congestionModel.get("batch_size");
  }
}
