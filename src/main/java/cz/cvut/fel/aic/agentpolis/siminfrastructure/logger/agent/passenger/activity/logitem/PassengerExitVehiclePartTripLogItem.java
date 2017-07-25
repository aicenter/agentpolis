package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

/**
 * The log item which signalizes that passenger exited from vehicle and did a
 * part of trip
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerExitVehiclePartTripLogItem extends PassengerExitVehicleLogItem {

    public PassengerExitVehiclePartTripLogItem(String passengerId, long simulationTimeId,
            long exitPositionByNodeId) {
        super(passengerId, simulationTimeId, exitPositionByNodeId);
        // TODO Auto-generated constructor stub
    }

}
