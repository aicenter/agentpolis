package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.callback.TeleportCallback;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * Action provides possibility teleport agent from one position to another.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class TeleportAgentAction {

    private final EventProcessor eventProcessor;
    private final AgentPositionModel agentPositionModel;
    private final EntityVelocityModel maxEntityVelocityStorage;

    @Inject
    public TeleportAgentAction(EventProcessor eventProcessor, AgentPositionModel agentPositionModel,
            EntityVelocityModel maxEntityVelocityStorage) {        
        super();
        this.eventProcessor = eventProcessor;
        this.agentPositionModel = agentPositionModel;
        this.maxEntityVelocityStorage = maxEntityVelocityStorage;
    }

    /**
     * Teleports agent from current position to destination position and
     * teleport time is computed based on distance between positions and agent
     * velocity.
     * 
     * @param distToSpatial
     * @param teleportCallback
     */
    public void teleportAgentToSpatial(final String agentId, double distToSpatial,
            final TeleportCallback teleportCallback) {

        long duration = computerDuration(agentId, distToSpatial);

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                teleportCallback.entityWasTeleported();

            }

            @Override
            public EventProcessor getEventProcessor() {
                // TODO Auto-generated method stub
                return null;
            }
        }, duration);
    }

    /**
     * Teleports agent from current position to destination position and
     * teleport time is computed based on distance between positions and agent
     * velocity.
     * 
     * @param destinationByNodeId
     * @param distBetweenPlacesInMeter
     * @param teleportCallback
     */
    public void teleportAgentToNode(final String agentId, final int destinationByNodeId,
            double distBetweenPlacesInMeter, final TeleportCallback teleportCallback) {

        long duration = computerDuration(agentId, distBetweenPlacesInMeter);

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                agentPositionModel.setNewEntityPosition(agentId, destinationByNodeId);
                teleportCallback.entityWasTeleported();

            }

            @Override
            public EventProcessor getEventProcessor() {
                // TODO Auto-generated method stub
                return null;
            }
        }, duration);

    }

    /**
     * Computes time based on distance between positions and agent velocity.
     * 
     * @param distBetweenPlacesInMeter
     * @return
     */
    private long computerDuration(final String agentId, double distBetweenPlacesInMeter) {
        double agentVelocityInMps = maxEntityVelocityStorage.getEntityVelocityInmps(agentId);
        return MoveUtil.computeDuration(agentVelocityInMps, distBetweenPlacesInMeter);

    }

}
