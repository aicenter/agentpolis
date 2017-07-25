/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.DefaultVisioInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.HighwayLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.NodeIdLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.Projection;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.SimulationControlLayer;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.VisManager;
import cz.agents.alite.vis.layer.common.ColorLayer;
import java.awt.Color;

/**
 *
 * @author fido
 */
@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer{
    
    private final HighwayLayer highwayLayer;
    
    private final TestVehicleLayer testVehicleLayer;
    
    private final NodeIdLayer nodeIdLayer;
    
    @Inject
    public TestVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
            HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
            RailwayNetwork railwayNetwork, AgentStorage agentStorage, VehicleStorage vehicleStorage, 
            AgentPositionModel agentPositionModel, VehiclePositionModel vehiclePositionModel, 
            AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator, 
            SimulationControlLayer simulationControlLayer, Projection projection, HighwayLayer highwayLayer,
            TestVehicleLayer testVehicleLayer, NodeIdLayer nodeIdLayer) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                agentStorage, vehicleStorage, agentPositionModel, vehiclePositionModel, allNetworkNodes, 
                simulationCreator, simulationControlLayer, projection);
        this.highwayLayer = highwayLayer;
        this.testVehicleLayer = testVehicleLayer;
        this.nodeIdLayer = nodeIdLayer;
    }
    
    @Override
    protected void initGraphLayers(Projection projection) {
        VisManager.registerLayer(ColorLayer.create(Color.white));
        VisManager.registerLayer(highwayLayer);
    }

    @Override
    protected void initEntityLayers(Simulation simulation, Projection projection) {
//        super.initEntityLayers(simulation, projection); 
        VisManager.registerLayer(testVehicleLayer); 
    }
    
    @Override
    protected void initLayersAfterEntityLayers() {
        VisManager.registerLayer(nodeIdLayer);
    }
    
    
}
