package cz.agents.agentpolis.siminfrastructure.logger.agent.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.siminfrastructure.logger.Logger;
import cz.agents.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem.EndDrivingLogItem;
import cz.agents.agentpolis.siminfrastructure.logger.agent.activity.logitem.StartDrivingLogItem;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The specific implementation of {@code Logger} relating to logging information
 * from {@code VehicleDrivingActivity}
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleDrivingActivityLogger extends Logger {

    @Inject
    public VehicleDrivingActivityLogger(PublishSubscribeLogger publishSubscribeLogger,
            EventProcessor eventProcessor) {
        super(publishSubscribeLogger, eventProcessor);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * Logs that a particular agent ended to driver a vehicle
     * 
     * @param driverId
     * @param vehicleId
     */
    public void logEndDriving(String driverId, String vehicleId) {
        log(new EndDrivingLogItem(getCurrentSimulationTime(), driverId, vehicleId));
    }

    /**
     * 
     * Logs that a particular agent started to driver a vehicle
     * 
     * @param driverId
     * @param vehicleId
     */
    public void logStartDriving(String driverId, String vehicleId) {
        log(new StartDrivingLogItem(getCurrentSimulationTime(), driverId, vehicleId));
    }

}
