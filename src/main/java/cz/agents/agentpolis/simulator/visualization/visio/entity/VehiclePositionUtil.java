/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;

/**
 *
 * @author fido
 */
@Singleton
public class VehiclePositionUtil extends EntityPositionUtil{
    
    @Inject
    public VehiclePositionUtil(PositionUtil positionUtil, VehiclePositionModel entityPositionModel, 
            AllNetworkNodes allNetworkNodes, Projection projection, EntityStorage entityStorage) {
        super(positionUtil, entityPositionModel, allNetworkNodes, projection, entityStorage);
    }
    
}
