package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * New terminology: MoveEntityAction includes both MoveAgent and MoveVehicle
 * 
 * Action moves some entity from one position to other and sets this information
 * to position storage
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class MoveEntityAction {

    private final EventProcessor eventProcessor;

    @Inject
    public MoveEntityAction(EventProcessor eventProcessor) {
        super();
        this.eventProcessor = eventProcessor;
    }

    /**
     * The method invokes a move action, which will change the state of the
     * environment (position of the entity) after the moving duration passes.
     * 
     */
    public void moveToNode(final String entityId, final int destinationByNodeId, long duration,
            final EntityPositionModel entityPositionStorage) {

        eventProcessor.addEvent(new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                entityPositionStorage.setNewEntityPosition(entityId, destinationByNodeId);

            }

            @Override
            public EventProcessor getEventProcessor() {
                return eventProcessor;
            }
        }, duration);

    }

}
