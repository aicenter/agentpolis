package cz.cvut.fel.aic.agentpolis.simmodel.environment.model;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.Agent;

/**
 * 
 * The storage of all initialized agents in simulation model
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public final class AgentStorage extends EntityStorage<Agent> {

    @Inject
    public AgentStorage() {
        super();
    }
}
