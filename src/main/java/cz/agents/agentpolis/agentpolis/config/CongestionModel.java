package cz.agents.agentpolis.agentpolis.config;

import java.lang.Integer;
import java.util.HashMap;

public class CongestionModel {
  public Integer randomSeed;

  public Integer connectionTickLength;

  public Integer maxFlowPerLane;

  public Integer batchSize;

  public Integer defaultCrossroadDrivingLanes;

  public CongestionModel(HashMap congestionModel) {
    this.randomSeed = (Integer) congestionModel.get("random_seed");
    this.connectionTickLength = (Integer) congestionModel.get("connection_tick_length");
    this.maxFlowPerLane = (Integer) congestionModel.get("max_flow_per_lane");
    this.batchSize = (Integer) congestionModel.get("batch_size");
    this.defaultCrossroadDrivingLanes = (Integer) congestionModel.get("default_crossroad_driving_lanes");
  }
}
