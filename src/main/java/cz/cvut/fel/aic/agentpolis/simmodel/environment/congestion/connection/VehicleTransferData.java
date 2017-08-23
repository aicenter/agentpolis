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
public class VehicleTransferData extends VehicleEventData{
    public final Lane from;

    public final Lane to;

    public final VehicleTripData vehicleTripData;

    public VehicleTransferData(Lane from, Lane to, VehicleTripData vehicleTripData, long transferFinishTime) {
        super(transferFinishTime);
        this.from = from;
        this.to = to;
        this.vehicleTripData = vehicleTripData;
    }
}
