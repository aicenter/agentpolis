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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Connection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;

import java.security.ProviderException;

/**
 * @author fido
 */
@Singleton
public class TestCongestionModel extends CongestionModel {

    @Inject
    public TestCongestionModel(TransportNetworks transportNetworks, Config config,
                               SimulationProvider simulationProvider, TimeProvider timeProvider, PositionUtil positionUtil, ShapeUtils shapeUtils) throws ModelConstructionFailedException,
            ProviderException {
        super(transportNetworks, config, simulationProvider, timeProvider, positionUtil, shapeUtils);
    }

    public Connection getConnectionByNode(SimulationNode node) {
        return connectionsMappedByNodes.get(node);
    }

    public Link getLinkByEdge(SimulationEdge edge) {
        return linksMappedByEdges.get(edge);
    }

}
