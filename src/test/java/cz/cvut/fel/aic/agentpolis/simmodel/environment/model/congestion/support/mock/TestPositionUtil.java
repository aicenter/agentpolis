/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;

/**
 *
 * @author fido
 */
@Singleton
public class TestPositionUtil extends PositionUtil{
    
    @Inject
    public TestPositionUtil(AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator, 
            HighwayNetwork highwayNetwork, TimeProvider timeProvider, Config config) {
        super(null, allNetworkNodes, simulationCreator, highwayNetwork, timeProvider, config);
    }
    
}
