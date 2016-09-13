package cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

public class MovementArrivalLogItem implements LogItem {

    public final String agentId;
    public final long simulationTime;
    public final int arrivedFromNodeId;
    public final int arrivedToNodeId;
    public final GraphType graphType;

    public MovementArrivalLogItem(String agentId, long simulationTime,
            int arrivedFromPositionByNodeId, int arrivedToPositionByNodeId, GraphType graphType) {
        super();
        this.agentId = agentId;
        this.simulationTime = simulationTime;
        this.arrivedFromNodeId = arrivedFromPositionByNodeId;
        this.arrivedToNodeId = arrivedToPositionByNodeId;
        this.graphType = graphType;
    }

}
