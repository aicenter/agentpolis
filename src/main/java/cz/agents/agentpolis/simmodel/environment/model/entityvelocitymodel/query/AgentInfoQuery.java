package cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.query;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;

/**
 * 
 * This sensor provides information about agent, which using this sensor. (?Push
 * sensor)
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class AgentInfoQuery {

    private final EntityVelocityModel entityVelocityStorage;

    @Inject
    public AgentInfoQuery(EntityVelocityModel entityVelocityStorage) {
        super();
        this.entityVelocityStorage = entityVelocityStorage;
    }

    /**
     * Provides the allowed speed for given agent
     * 
     * @param agentId
     * @return - speed in meter per second
     */
    public double getCurrrentAgentVelocity(String agentId) {
        return entityVelocityStorage.getEntityVelocityInmps(agentId);
    }

}
