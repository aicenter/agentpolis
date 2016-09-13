package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

/**
 * The log item which signalizes that passenger exited from PT vehicle and did
 * the full trip
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerExitPTVehicleFullTripLogItem extends PassengerExitVehicleLogItem {

    public final String lineId;

    public PassengerExitPTVehicleFullTripLogItem(String passengerId, long simulationTime,
            long exitPositionByNodeId, String lineId) {
        super(passengerId, simulationTime, exitPositionByNodeId);
        this.lineId = lineId;
    }

}
