package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.alite.common.event.EventProcessor;

/**
 * Agent position action - agent from agent position environment
 *
 * @author Zbynek Moler
 */
@Singleton
public class AgentPositionAction extends APositionAction {

    @Inject
    public AgentPositionAction(EventProcessor eventProcessor, AgentPositionModel agentPositionModel) {
        super(eventProcessor, agentPositionModel);

    }

    public boolean setTargetPosition(String entityId, int nodeId) {
        return getEntityPositionStorage().setTargetPositionAndReturnIfWasSame(entityId, nodeId);
    }

}
