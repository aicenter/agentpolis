/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.agents.alite.simulation.Simulation;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.VisManager;
import cz.agents.alite.vis.layer.common.FpsLayer;
import cz.agents.alite.vis.layer.common.HelpLayer;
import cz.agents.alite.vis.layer.common.VisInfoLayer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;
import cz.cvut.fel.aic.geographtools.util.Utils2D;
import javax.vecmath.Point2d;


/**
 *
 * @author F-I-D-O
 */
public class DefaultVisioInitializer implements VisioInitializer{
	
	private final PedestrianNetwork pedestrianNetwork;
	
	private final BikewayNetwork bikewayNetwork;
	
	private final HighwayNetwork highwayNetwork;
	
	private final TramwayNetwork tramwayNetwork;
	
	private final MetrowayNetwork metrowayNetwork;
	
	private final RailwayNetwork railwayNetwork;
    
    private final SimulationControlLayer simulationControlLayer;
    
    private final GridLayer gridLayer;
    
    
    

	@Inject
	public DefaultVisioInitializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork, 
			HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork, 
			RailwayNetwork railwayNetwork, SimulationControlLayer simulationControlLayer, GridLayer gridLayer) {
		this.pedestrianNetwork = pedestrianNetwork;
		this.bikewayNetwork = bikewayNetwork;
		this.highwayNetwork = highwayNetwork;
		this.tramwayNetwork = tramwayNetwork;
		this.metrowayNetwork = metrowayNetwork;
		this.railwayNetwork = railwayNetwork;
        this.simulationControlLayer = simulationControlLayer;
        this.gridLayer = gridLayer;
	}
	
	

	@Override
	public void initialize(Simulation simulation) {
        initWindow();
        initGraphLayers();
        initLayersBeforeEntityLayers();
        initEntityLayers(simulation);
		initLayersAfterEntityLayers();
        initInfoLayers();
	}

    protected void initGraphLayers() {
        VisManager.registerLayer(gridLayer);
    }

    protected void initEntityLayers(Simulation simulation) {

    }

    protected void initLayersAfterEntityLayers() {
        
    }

    protected void initLayersBeforeEntityLayers() {
        
    }

    protected void initInfoLayers() {
        VisManager.registerLayer(HelpLayer.create());
        VisManager.registerLayer(FpsLayer.create());
        VisManager.registerLayer(VisInfoLayer.create());
        
        // VisManager.registerLayer(LogoLayer.create(ResourceReader.getPathToResource("/img/atg_blue.png")));
        
        VisManager.registerLayer(simulationControlLayer);
    }

    private void initWindow() {
        final int windowHight = 1000;
        final int windowWidth = 1900;
        
        VisManager.setInvertYAxis(true);

        VisManager.setInitParam("Agentpolis operator", windowWidth, windowHight);
        

//        final double zoomFactor = windowWidth / projection.sceneWidth;
        
//        final double zoomFactor = 1.2;

        VisManager.setSceneParam(new VisManager.SceneParams() {

            @Override
            public double getDefaultZoomFactor() {
                return (double) 1900 / Utils2D.getGraphWidth(highwayNetwork.getNetwork());
            }
            
            

//            @Override
//            public Rectangle getWorldBounds() {
//                return new Rectangle(projection.sceneWidth, projection.sceneHeight);
//            }

            @Override
            public Point2d getDefaultLookAt() {
                GPSLocation centroid = Utils2D.getGraphCentroid(highwayNetwork.getNetwork());
                Point2d centerPoint = new Point2d(centroid.getLongitudeProjected(), 
                        centroid.getLatitudeProjected());
                return centerPoint;
            }

        });
        
        
        
        VisManager.init();
    }
	
}
