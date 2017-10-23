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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;

import java.security.ProviderException;

/**
 * @author fido
 */
@Singleton
public class TestCongestionModel extends CongestionModel {

    @Inject
    public TestCongestionModel(TransportNetworks transportNetworks, AgentpolisConfig config,
                               SimulationProvider simulationProvider, TimeProvider timeProvider, ShapeUtils shapeUtils, LaneCongestionModel laneCongestionModel) throws ModelConstructionFailedException,
            ProviderException {
        super(transportNetworks, config, simulationProvider, timeProvider, shapeUtils, laneCongestionModel);
    }

    public Connection getConnectionByNode(SimulationNode node) {
        return connectionsMappedByNodes.get(node);
    }

    public Link getLinkByEdge(SimulationEdge edge) {
        return linksMappedByEdges.get(edge);
    }

}
