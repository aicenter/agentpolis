package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.Logger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.EndWalkingLogItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.StartWalkingLogItem;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The specific implementation of {@code Logger} relating to logging information
 * from {@code WalkingActivity}
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class WalkingActivityLogger extends Logger {

    @Inject
    public WalkingActivityLogger(PublishSubscribeLogger publishSubscribeLogger,
            EventProcessor eventProcessor) {
        super(publishSubscribeLogger, eventProcessor);
    }

    /**
     * 
     * Logs that a particular agent ended to walk
     * 
     * @param walkerId
     */
    public void logEndWalking(String walkerId) {
        log(new EndWalkingLogItem(walkerId, getCurrentSimulationTime()));
    }

    /**
     * 
     * 
     * 
     * @param walkerId
     */
    public void logStartWalking(String walkerId) {
        log(new StartWalkingLogItem(walkerId, getCurrentSimulationTime()));
    }
}
