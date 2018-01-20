/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEventData;

public class VehicleCTMEndData extends VehicleEventData {
    public final Segment segment;

    public VehicleCTMEndData(Segment segment, long endingFinishTime) {
        super(endingFinishTime);
        this.segment = segment;
    }
}
