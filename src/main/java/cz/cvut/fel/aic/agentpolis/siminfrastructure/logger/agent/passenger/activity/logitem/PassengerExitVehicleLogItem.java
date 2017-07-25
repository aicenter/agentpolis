package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.ALogItem;

/**
 * The log item which signalizes that passenger exited from PT vehicle
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerExitVehicleLogItem extends ALogItem {

    public final long exitPositionByNodeId;

    public PassengerExitVehicleLogItem(String passengerId, long simulationTime,
            long exitPositionByNodeId) {
        super(passengerId, simulationTime);
        this.exitPositionByNodeId = exitPositionByNodeId;
    }

}
