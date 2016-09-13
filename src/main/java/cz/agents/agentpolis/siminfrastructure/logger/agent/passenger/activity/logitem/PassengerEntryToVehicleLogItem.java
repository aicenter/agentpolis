package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.ALogItem;

/**
 * The log item which signalizes that passenger entered to a vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerEntryToVehicleLogItem extends ALogItem {

    public final long positionByNodeId;

    public PassengerEntryToVehicleLogItem(String agentId, long simulationTime,
            long positionByNodeId) {
        super(agentId, simulationTime);
        this.positionByNodeId = positionByNodeId;
    }

}
