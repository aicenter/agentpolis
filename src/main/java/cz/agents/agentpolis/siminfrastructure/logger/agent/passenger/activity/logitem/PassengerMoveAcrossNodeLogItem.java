package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.ALogItem;

/**
 * The log item which signalizes that passenger moved across a particular node
 * during the trip execution
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerMoveAcrossNodeLogItem extends ALogItem {
    public final long positionByNodeId;

    public PassengerMoveAcrossNodeLogItem(String passengerId, long simulationTime,
            long positionByNodeId) {
        super(passengerId, simulationTime);
        this.positionByNodeId = positionByNodeId;
    }

}
