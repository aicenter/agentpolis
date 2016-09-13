package cz.agents.agentpolis.simmodel.environment.model.action;

import cz.agents.alite.common.event.EventProcessor;

/**
 * 
 * Agent polis action
 * 
 * @author Zbynek Moler
 * 
 * @param <TEnvironment>
 * @param <TEntity>
 */
public abstract class AgentPolisAction {

    protected EventProcessor eventProcessor;

    public AgentPolisAction(EventProcessor eventProcessor) {
        super();
        this.eventProcessor = eventProcessor;
    }

}
