/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.DefaultVisioInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.HighwayLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.NodeIdLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.SimulationControlLayer;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.GridLayer;
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
    
    private final CarLayer carLayer;
    
    @Inject
    public TestVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
            HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
            RailwayNetwork railwayNetwork, 
            SimulationControlLayer simulationControlLayer, HighwayLayer highwayLayer,
            TestVehicleLayer testVehicleLayer, NodeIdLayer nodeIdLayer, GridLayer gridLayer, CarLayer carLayer) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                simulationControlLayer, gridLayer);
        this.highwayLayer = highwayLayer;
        this.testVehicleLayer = testVehicleLayer;
        this.nodeIdLayer = nodeIdLayer;
        this.carLayer = carLayer;
    }
    
    @Override
    protected void initGraphLayers() {
        VisManager.registerLayer(ColorLayer.create(Color.white));
        super.initGraphLayers();
        VisManager.registerLayer(highwayLayer);
    }

    @Override
    protected void initEntityLayers(Simulation simulation) {
//        super.initEntityLayers(simulation, projection); 
        VisManager.registerLayer(carLayer); 
    }
    
    @Override
    protected void initLayersAfterEntityLayers() {
        VisManager.registerLayer(nodeIdLayer);
    }
    
    
}
