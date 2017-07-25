package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent started driving a particular
 * vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class StartDrivingLogItem implements LogItem {

    public final long simulationTime;
    public final String driverId;
    public final String vehicleId;

    public StartDrivingLogItem(long simulationTime, String driverId, String vehicleId) {
        super();
        this.simulationTime = simulationTime;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

}
