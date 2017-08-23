/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.VehicleTripData;

/**
 *
 * @author fido
 */
public class VehicleEndData extends VehicleEventData{
    public final Lane lane;

    public final VehicleTripData vehicleTripData;


    public VehicleEndData(Lane lane, VehicleTripData vehicleTripData, long endingFinishTime) {
        super(endingFinishTime);
        this.lane = lane;
        this.vehicleTripData = vehicleTripData;
    }
}
