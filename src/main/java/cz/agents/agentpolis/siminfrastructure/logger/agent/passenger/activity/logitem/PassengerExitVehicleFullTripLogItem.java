package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

/**
 * The log item which signalizes that passenger exited from a vehicle and did
 * the full trip
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerExitVehicleFullTripLogItem extends PassengerExitVehicleLogItem {

    public PassengerExitVehicleFullTripLogItem(String passengerId, long simulationTimeId,
            long exitPositionByNodeId) {
        super(passengerId, simulationTimeId, exitPositionByNodeId);
        // TODO Auto-generated constructor stub
    }

}
