/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

/**
 *
 * @author fido
 */
public class VehicleQueueData {
    private final VehicleTripData vehicleTripData;
    
    private long minPollTime;

    public VehicleTripData getVehicleTripData() {
        return vehicleTripData;
    }

    public long getMinPollTime() {
        return minPollTime;
    }

    public void setMinPollTime(long minPollTime) {
        this.minPollTime = minPollTime;
    }
    
    
    

    public VehicleQueueData(VehicleTripData vehicleTripData, long minPollTime) {
        this.vehicleTripData = vehicleTripData;
        this.minPollTime = minPollTime;
    }
    
    
}
