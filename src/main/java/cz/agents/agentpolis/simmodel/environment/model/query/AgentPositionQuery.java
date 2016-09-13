package cz.agents.agentpolis.simmodel.environment.model.query;

import javax.inject.Singleton;

import com.google.inject.Inject;

import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.TransportNetworks;

/**
 * 
 * Agent position query for getting information about agent position
 * 
 * @author Zbynek Moler
 *
 */
@Singleton
public class AgentPositionQuery extends APositionQuery {

    @Inject
    public AgentPositionQuery(AgentPositionModel agentPositionModel,
            TransportNetworks transportNetworks) {
        super(agentPositionModel, transportNetworks);
    }
}
