/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.HighwayNetwork;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;

/**
 *
 * @author fido
 */
@Singleton
public class AgentPositionUtil extends EntityPositionUtil{
    
    @Inject
    public AgentPositionUtil(PositionUtil positionUtil, AgentPositionModel entityPositionModel, 
            AllNetworkNodes allNetworkNodes, Projection projection, EntityStorage entityStorage, 
            HighwayNetwork highwayNetwork) {
        super(positionUtil, entityPositionModel, allNetworkNodes, projection, entityStorage, highwayNetwork);
    }
    
}
