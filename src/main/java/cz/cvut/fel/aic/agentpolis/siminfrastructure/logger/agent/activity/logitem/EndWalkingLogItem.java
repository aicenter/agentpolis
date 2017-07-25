package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent ended to walk
 * 
 * @author Zbynek Moler
 * 
 */
public class EndWalkingLogItem implements LogItem {

    /**
     * Walker's id
     */
    public final String agentId;
    public final long simulationTime;

    public EndWalkingLogItem(String agentId, long simulationTime) {
        super();
        this.agentId = agentId;
        this.simulationTime = simulationTime;
    }

}
