package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEventData;

/**
 * Created by martin on 1/19/18.
 */
public class VehicleCTMTransferData extends VehicleEventData {
    public Segment fromSegment;
    public Segment toSegment;

    public VehicleCTMTransferData(Segment fromSegment, Segment toSegment, long transferFinishTime) {
        super(transferFinishTime);
        this.fromSegment = fromSegment;
        this.toSegment = toSegment;
    }
}
