package cz.agents.agentpolis.simmodel.environment.model.action;

import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.sensor.PositionUpdated;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * Position action
 *
 * @author Zbynek Moler
 */
public abstract class APositionAction {

    private final EventProcessor eventProcessor;
    private final EntityPositionModel entityPositionStorage;

    public APositionAction(EventProcessor eventProcessor, EntityPositionModel entityPositionStorage) {
        super();
        this.eventProcessor = eventProcessor;
        this.entityPositionStorage = entityPositionStorage;
    }

    /**
     * Set new position of related entity.
     * <p/>
     * Unsafe methods doesn't check if the node is form proper storage.
     *
     * @param nodeId   New position.
     * @param silent When true the position sensing is omitted.
     */
    public void actSetPositionUnsafe(String entityId, int nodeId, boolean silent) {
        entityPositionStorage.setNewEntityPosition(entityId, nodeId, silent);
    }

    /**
     * Set new position of related entity to node form proper storage.
     *
     * @param nodeId Id of new position.
     */
    public void actSetPosition(final String entityId, int nodeId) {
        actSetPosition(entityId, nodeId, false);
    }

    /**
     * Set new position of related entity to node form proper storage.
     *
     * @param nodeId Id of new position.
     * @param silent When true the position sensing is omitted.
     */
    public void actSetPosition(final String entityId, int nodeId, boolean silent) {
        actSetPositionUnsafe(entityId, nodeId, silent);
    }

    /**
     * Remove entity position from its position storage.
     */
    public void actRemovePosition(final String entityId) {
        eventProcessor.addEvent(new EventHandler() {

            public void handleEvent(Event event) {
                entityPositionStorage.removeEntity(entityId);

            }

            public EventProcessor getEventProcessor() {
                // TODO Auto-generated method stub
                return null;
            }
        });

    }

    public void addSensingPositionNode(final String entityId, PositionUpdated positionSensor) {

        entityPositionStorage.addSensingPositionNode(entityId, positionSensor);

    }

    public void removeSensingPositionNode(final String entityId,
                                          PositionUpdated positionSensor) {
        entityPositionStorage.removeSensingPositionNode(entityId, positionSensor);
    }


    protected EntityPositionModel getEntityPositionStorage() {
        return entityPositionStorage;
    }
}
