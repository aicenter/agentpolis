package cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleOccupancyDuringMovingLogItem implements LogItem {

    public final long simulationTime;
    public final String vehicleId;
    public final long fromNodeId;
    public final long toNodeId;
    public final int numberOfPassengerInVehicle;

    public VehicleOccupancyDuringMovingLogItem(long simulationTime, String vehicleId,
            long fromPositionByNodeId, long toPositionByNodeId, int numberOfPassengerInVehicle) {
        super();
        this.simulationTime = simulationTime;
        this.vehicleId = vehicleId;
        this.fromNodeId = fromPositionByNodeId;
        this.toNodeId = toPositionByNodeId;
        this.numberOfPassengerInVehicle = numberOfPassengerInVehicle;
    }
}
