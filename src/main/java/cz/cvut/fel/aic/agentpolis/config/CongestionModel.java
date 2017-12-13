package cz.cvut.fel.aic.agentpolis.config;

import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.util.Map;

public class CongestionModel {
  public Integer randomSeed;

  public Double maxFlowPerLane;

  public Integer batchSize;

  public Double criticalDensity;

  public Boolean fundamentalDiagramDelay;

  public Integer defaultCrossroadDrivingLanes;

  public CongestionModel(Map congestionModel) {
    this.randomSeed = (Integer) congestionModel.get("random_seed");
    this.maxFlowPerLane = (Double) congestionModel.get("max_flow_per_lane");
    this.batchSize = (Integer) congestionModel.get("batch_size");
    this.criticalDensity = (Double) congestionModel.get("critical_density");
    this.fundamentalDiagramDelay = (Boolean) congestionModel.get("fundamental_diagram_delay");
    this.defaultCrossroadDrivingLanes = (Integer) congestionModel.get("default_crossroad_driving_lanes");
  }
}
