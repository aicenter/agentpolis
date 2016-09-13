package cz.agents.agentpolis.siminfrastructure.logger.agent.passenger.activity.logitem;

public class PassengerMissPTVehicleLogItem extends PassengerMissVehicleLogItem {

    public final String lineId;

    public PassengerMissPTVehicleLogItem(String publisherId, long simulationTime, String lineId) {
        super(publisherId, simulationTime);
        this.lineId = lineId;
    }

}
