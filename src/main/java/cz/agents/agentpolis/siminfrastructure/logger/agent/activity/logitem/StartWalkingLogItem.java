package cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.LogItem;

/**
 * The log item which signalizes that an agent started to walk
 * 
 * @author Zbynek Moler
 * 
 */
public class StartWalkingLogItem implements LogItem {

    /**
     * Walker's id
     */
    public final String agentId;
    public final long simulationTime;

    public StartWalkingLogItem(String agentId, long simulationTime) {
        super();
        this.agentId = agentId;
        this.simulationTime = simulationTime;
    }

}
