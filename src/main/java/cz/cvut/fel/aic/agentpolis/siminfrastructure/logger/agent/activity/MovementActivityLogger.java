package cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.Logger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.PublishSubscribeLogger;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.agent.activity.logitem.MovementArrivalLogItem;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * The specific implementation of {@code Logger} relating to logging information
 * from {@code MovementActivity}
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class MovementActivityLogger extends Logger {

    @Inject
    public MovementActivityLogger(PublishSubscribeLogger publishSubscribeLogger,
            EventProcessor eventProcessor) {
        super(publishSubscribeLogger, eventProcessor);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * Logs the information about arrival to particular position from previous
     * position
     * 
     * @param agentId
     * @param arrivedFromPositionByNodeId
     * @param arrivedToPositionByNodeId
     * @param graphType
     */
    public void logMovementArrivalLogItem(final String agentId,
                                          final int arrivedFromPositionByNodeId, final int arrivedToPositionByNodeId,
            final GraphType graphType) {
        log(new MovementArrivalLogItem(agentId, getCurrentSimulationTime(),
                arrivedFromPositionByNodeId, arrivedToPositionByNodeId, graphType));
    }

}
