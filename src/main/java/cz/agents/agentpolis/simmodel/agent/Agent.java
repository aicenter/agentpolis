package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;

/**
 * 
 * The agents in a simulation model are extended by this an abstract
 * representation.
 * 
 * 
 * @author Zbynek Moler
 */
public abstract class Agent extends AgentPolisEntity {

    private final EntityType type;

    public Agent(final String agentId, final EntityType agentType) {
        super(agentId);
        this.type = agentType;
    }

    public abstract void born();

    public EntityType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Agent " + getType().toString() + " " + getId();
    }

}
