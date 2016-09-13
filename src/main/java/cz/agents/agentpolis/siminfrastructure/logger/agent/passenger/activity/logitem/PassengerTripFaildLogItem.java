package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.ALogItem;

/**
 * The log item which signalizes that passenger is not able to failed to execute
 * a particular trip
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerTripFaildLogItem extends ALogItem {

    public PassengerTripFaildLogItem(String publisherId, long simulationTime) {
        super(publisherId, simulationTime);
        // TODO Auto-generated constructor stub
    }

}
