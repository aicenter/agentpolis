package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

/**
 * The log item which signalizes that passenger exited from PT vehicle and did a
 * part of trip
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerExitPTVehiclePartTripLogItem extends PassengerExitVehicleLogItem {

    public final String lineId;

    public PassengerExitPTVehiclePartTripLogItem(String passengerId, long simulationTime,
            long exitPositionByNodeId, String lineId) {
        super(passengerId, simulationTime, exitPositionByNodeId);
        this.lineId = lineId;
    }

}
