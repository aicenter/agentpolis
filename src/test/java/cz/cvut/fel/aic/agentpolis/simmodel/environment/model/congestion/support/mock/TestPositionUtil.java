/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;

/**
 * @author fido
 */
@Singleton
public class TestPositionUtil extends PositionUtil {

    @Inject
    public TestPositionUtil(AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator,
                            HighwayNetwork highwayNetwork, PedestrianNetwork pedestrianNetwork, TimeProvider timeProvider, AgentpolisConfig config, GraphSpec2D mapSpecification, ShapeUtils shapeUtils) {
        super(allNetworkNodes, highwayNetwork, pedestrianNetwork, timeProvider, shapeUtils);
    }

}
