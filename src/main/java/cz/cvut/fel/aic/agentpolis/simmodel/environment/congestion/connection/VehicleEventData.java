/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

/**
 * @author fido
 */
public class VehicleEventData {
    public final long transferFinishTime;
    /**
     * -2 if it is transfer from lane to lane
     */
    public long laneId;

    public VehicleEventData(long transferFinishTime, long laneId) {
        this.transferFinishTime = transferFinishTime;
        this.laneId = laneId;
    }
}
