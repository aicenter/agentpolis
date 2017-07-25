package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes about a vehicle movement plan
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleMovePlanLogItem implements LogItem {

    public final String vehicleId;
    public final long currentSimulationTime;
    public final int fromByNodeId;
    public final int toByNodeId;

    public VehicleMovePlanLogItem(String vehicleId, long currentSimulationTime, int fromByNodeId,
                                  int toByNodeId) {
        super();
        this.vehicleId = vehicleId;
        this.currentSimulationTime = currentSimulationTime;
        this.fromByNodeId = fromByNodeId;
        this.toByNodeId = toByNodeId;
    }
}
