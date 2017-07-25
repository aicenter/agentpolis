package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.query;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;

import javax.inject.Singleton;

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
