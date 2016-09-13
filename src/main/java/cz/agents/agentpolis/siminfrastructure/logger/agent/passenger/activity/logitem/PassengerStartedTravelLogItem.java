package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

import cz.agents.agentpolis.siminfrastructure.logger.ALogItem;

/**
 * The log item which signalizes that passenger started traveling
 * 
 * @author Zbynek Moler
 * 
 */
public class PassengerStartedTravelLogItem extends ALogItem {
    public final double longtitude;
    public final double latitude;

    public PassengerStartedTravelLogItem(String publisherId, long simulationTime,
            double longtitude, double latitude) {
        super(publisherId, simulationTime);
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

}
