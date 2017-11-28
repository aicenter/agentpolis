/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleTripData;

/**
 * @author fido
 */
public class VehicleEndData extends VehicleEventData {
    public final CongestionLane congestionLane;
    public final VehicleTripData vehicleTripData;

    public VehicleEndData(CongestionLane congestionLane, VehicleTripData vehicleTripData, long endingFinishTime, long laneId) {
        super(endingFinishTime, laneId);
        this.congestionLane = congestionLane;
        this.vehicleTripData = vehicleTripData;
    }
}
