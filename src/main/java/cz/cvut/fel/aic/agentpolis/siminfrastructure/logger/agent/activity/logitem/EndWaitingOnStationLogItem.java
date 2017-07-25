package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent ended waiting on a station
 * 
 * @author Zbynek Moler
 * 
 */
public class EndWaitingOnStationLogItem implements LogItem {

    public final long simulationTime;
    public final String agentId;

    public EndWaitingOnStationLogItem(long simulationTime, String agentId) {
        super();
        this.simulationTime = simulationTime;
        this.agentId = agentId;
    }

}
