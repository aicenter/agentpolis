/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.*;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;

import java.awt.*;

/**
 * @author fido
 */
@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer {

    private final HighwayLayer highwayLayer;

    private final TestVehicleLayer testVehicleLayer;

    private final NodeIdLayer nodeIdLayer;

    private final CarLayer carLayer;
    private final VisLayer densityLayer;

    @Inject
    public TestVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork,
                                HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork,
                                RailwayNetwork railwayNetwork,
                                SimulationControlLayer simulationControlLayer, HighwayLayer highwayLayer,
                                TestVehicleLayer testVehicleLayer, NodeIdLayer nodeIdLayer, GridLayer gridLayer, CarLayer carLayer, TrafficDensityByDirectionLayer<DriveAgent, DriveAgentStorage> densityLayer) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                simulationControlLayer, gridLayer);
        this.highwayLayer = highwayLayer;
        this.testVehicleLayer = testVehicleLayer;
        this.nodeIdLayer = nodeIdLayer;
        this.carLayer = carLayer;
        this.densityLayer = densityLayer;
    }

    @Override
    protected void initGraphLayers() {
        VisManager.registerLayer(ColorLayer.create(Color.white));
        super.initGraphLayers();
        VisManager.registerLayer(highwayLayer);
        VisManager.registerLayer(densityLayer);
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
