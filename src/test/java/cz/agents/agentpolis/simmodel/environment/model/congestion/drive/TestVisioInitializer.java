/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.drive;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.AgentStorage;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.BikewayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.MetrowayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.PedestrianNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.RailwayNetwork;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TramwayNetwork;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.agentpolis.simulator.visualization.visio.DefaultVisioInitializer;
import cz.agents.agentpolis.simulator.visualization.visio.HighwayLayer;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.agentpolis.simulator.visualization.visio.SimulationControlLayer;
import cz.agents.alite.vis.VisManager;

/**
 *
 * @author fido
 */
@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer{
    
    private final HighwayLayer highwayLayer;
    
    @Inject
    public TestVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
            HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
            RailwayNetwork railwayNetwork, AgentStorage agentStorage, VehicleStorage vehicleStorage, 
            AgentPositionModel agentPositionModel, VehiclePositionModel vehiclePositionModel, 
            AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator, 
            SimulationControlLayer simulationControlLayer, Projection projection, HighwayLayer highwayLayer) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                agentStorage, vehicleStorage, agentPositionModel, vehiclePositionModel, allNetworkNodes, 
                simulationCreator, simulationControlLayer, projection);
        this.highwayLayer = highwayLayer;
    }
    
    @Override
    protected void initGraphLayers(Projection projection) {
        VisManager.registerLayer(highwayLayer);
    }
    
}
