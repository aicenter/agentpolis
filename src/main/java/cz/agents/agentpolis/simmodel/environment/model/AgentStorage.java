package cz.agents.agentpolis.simmodel.environment.model;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.entity.EntityType;

/**
 * 
 * The storage of all initialized agents in simulation model
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class AgentStorage extends EntityStorage<Agent> {

    @Inject
    public AgentStorage(Map<String, Agent> entities, Map<EntityType, Set<String>> entitiesByType) {
        super(entities, entitiesByType);
    }
}
