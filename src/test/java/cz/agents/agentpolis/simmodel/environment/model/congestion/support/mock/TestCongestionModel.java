/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.support.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simmodel.environment.model.congestion.CongestionModel;
import cz.agents.agentpolis.simmodel.environment.model.congestion.Connection;
import cz.agents.agentpolis.simmodel.environment.model.congestion.Link;
import cz.agents.agentpolis.simmodel.environment.model.congestion.ModelConstructionFailedException;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import java.security.ProviderException;

/**
 *
 * @author fido
 */
@Singleton
public class TestCongestionModel extends CongestionModel{
    
    @Inject
    public TestCongestionModel(TransportNetworks transportNetworks, Config config, 
            SimulationProvider simulationProvider, TimeProvider timeProvider, PositionUtil positionUtil) throws ModelConstructionFailedException,
            ProviderException {
        super(transportNetworks, config, simulationProvider, timeProvider, positionUtil);
    }
    
    public Connection getConnectionByNode(SimulationNode node){
        return connectionsMappedByNodes.get(node);
    }
    
    public Link getLinkByEdge(SimulationEdge edge){
        return linksMappedByEdges.get(edge);
    }
    
}
