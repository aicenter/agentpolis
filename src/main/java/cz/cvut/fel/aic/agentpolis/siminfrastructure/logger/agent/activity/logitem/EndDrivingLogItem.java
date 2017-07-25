package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent finished driving a particular
 * vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class EndDrivingLogItem implements LogItem {

    public final long simulationTime;
    public final String driverId;
    public final String vehicleId;

    public EndDrivingLogItem(long simulationTime, String driverId, String vehicleId) {
        super();
        this.simulationTime = simulationTime;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

}
