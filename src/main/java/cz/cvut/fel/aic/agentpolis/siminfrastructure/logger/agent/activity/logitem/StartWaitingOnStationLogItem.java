package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent started waiting on a station
 * 
 * @author Zbynek Moler
 * 
 */
public class StartWaitingOnStationLogItem implements LogItem {

    public final long simulationTime;
    public final String agentId;
    public final String stationByStationId;

    public StartWaitingOnStationLogItem(long simulationTime, String agentId,
            String stationByStationId) {
        super();
        this.simulationTime = simulationTime;
        this.agentId = agentId;
        this.stationByStationId = stationByStationId;
    }

}