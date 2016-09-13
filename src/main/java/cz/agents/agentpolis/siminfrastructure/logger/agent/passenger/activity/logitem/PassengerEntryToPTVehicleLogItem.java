package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

/**
 * The log item which signalizes that passenger entered to a PT vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerEntryToPTVehicleLogItem extends PassengerEntryToVehicleLogItem {

    public final String lineId;

    public PassengerEntryToPTVehicleLogItem(String agentId, long simulationTime,
            long positionByNodeId, String lineId) {
        super(agentId, simulationTime, positionByNodeId);
        this.lineId = lineId;
    }

}
