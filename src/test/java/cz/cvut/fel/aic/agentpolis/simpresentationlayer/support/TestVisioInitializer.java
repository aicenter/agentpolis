package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.*;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.KeyToggleLayer;

import javax.inject.Inject;
import java.awt.*;

@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer {

    protected final NodeIdLayer nodeIdLayer;
    protected final HighwayLayer highwayLayer;
    private final VisLayer backgroundLayer;
    private final MapTilesLayer mapTilesLayer;

    @Inject
    public TestVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork,
                                HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork,
                                RailwayNetwork railwayNetwork, NodeIdLayer nodeIdLayer, HighwayLayer highwayLayer,
                                SimulationControlLayer simulationControlLayer, GridLayer gridLayer, MapTilesLayer mapTiles) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                simulationControlLayer, gridLayer);
        this.nodeIdLayer = nodeIdLayer;
        this.highwayLayer = highwayLayer;
        this.backgroundLayer = ColorLayer.create(Color.white);
        this.mapTilesLayer = mapTiles;
    }

    @Override
    protected void initGraphLayers() {
        VisManager.registerLayer(backgroundLayer);
        VisManager.registerLayer(mapTilesLayer);
        VisManager.registerLayer(KeyToggleLayer.create("h", true, highwayLayer));
    }


    @Override
    protected void initLayersAfterEntityLayers() {
        VisManager.registerLayer(nodeIdLayer);
    }


}
