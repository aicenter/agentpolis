package cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.LogItem;

public class OvercrowdedPTVehicleLogItem implements LogItem {

    public final long simulationTime;
    public final String vehicleId;
    public final String stationId;

    public OvercrowdedPTVehicleLogItem(long simulationTime, String vehicleId, String stationId) {
        super();
        this.simulationTime = simulationTime;
        this.vehicleId = vehicleId;
        this.stationId = stationId;
    }

}
