/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
import java.lang.Double;
import java.lang.Integer;
import java.util.Map;

public class CongestionModel {
  public Integer randomSeed;

  public Double maxFlowPerLane;

  public Integer batchSize;

  public Boolean fundamentalDiagramDelay;

  public Integer defaultCrossroadDrivingLanes;

  public CongestionModel(Map congestionModel) {
    this.randomSeed = (Integer) congestionModel.get("random_seed");
    this.maxFlowPerLane = (Double) congestionModel.get("max_flow_per_lane");
    this.batchSize = (Integer) congestionModel.get("batch_size");
    this.fundamentalDiagramDelay = (Boolean) congestionModel.get("fundamental_diagram_delay");
    this.defaultCrossroadDrivingLanes = (Integer) congestionModel.get("default_crossroad_driving_lanes");
  }
}
