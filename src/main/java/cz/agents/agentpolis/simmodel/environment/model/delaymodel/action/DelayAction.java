package cz.agents.agentpolis.simmodel.environment.model.delaymodel.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayActor;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.DelayModel;

/**
 * Action provides logic for adding {@code DelayActor} to specific
 * {@code DelayingSegment} through {@code JunctionHandler}.
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class DelayAction {

    private final DelayModel delayHandler;

    @Inject
    public DelayAction(DelayModel delayHandler) {
        super();
        this.delayHandler = delayHandler;
    }

    /**
     * 
     * Adds {@code DelayActor} into {@code DelayModel} to be delayed based on
     * the current satiation
     * 
     * @param fromNodeById
     * @param toNodeById
     * @param graphType
     * @param delayAction
     */
    public void addDelayActor(long fromNodeById, long toNodeById, GraphType graphType,
            DelayActor delayAction) {
        delayHandler.handleDelayActor(fromNodeById, toNodeById, graphType, delayAction);
    }

}
