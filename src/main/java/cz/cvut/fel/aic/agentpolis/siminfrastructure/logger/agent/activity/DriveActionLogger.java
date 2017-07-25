package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.Logger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.VehicleMovePlanLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.VehicleOccupancyDuringMovingLogItem;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The specific implementation of {@code Logger} relating to logging information
 * from {@code DriveAction}
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class DriveActionLogger extends Logger {

    @Inject
    public DriveActionLogger(PublishSubscribeLogger publishSubscribeLogger,
            EventProcessor eventProcessor) {
        super(publishSubscribeLogger, eventProcessor);
    }

    /**
     * 
     * Logs the number of passenger for a particular vehicle between two
     * positions
     * 
     * @param vehicleId
     * @param passengerCount
     * @param startByNodeId
     * @param destByNodeId
     */
    public void logNumPassengers(String vehicleId, int passengerCount, int startByNodeId,
                                 int destByNodeId) {
        if (passengerCount > 1) {
            log(new VehicleOccupancyDuringMovingLogItem(getCurrentSimulationTime(), vehicleId,
                    startByNodeId, destByNodeId, passengerCount));

        }

    }

    /**
     * 
     * Logs the information about vehicle future movement from one position to
     * another
     * 
     * @param vehicleId
     * @param fromByNodeId
     * @param toByNodeId
     */
    public void logEntityMovePlan(String vehicleId, int fromByNodeId, int toByNodeId) {
        log(new VehicleMovePlanLogItem(vehicleId, getCurrentSimulationTime(), fromByNodeId,
                toByNodeId));
    }
}
