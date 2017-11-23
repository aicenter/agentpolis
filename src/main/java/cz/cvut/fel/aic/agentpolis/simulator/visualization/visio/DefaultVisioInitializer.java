/* 
 * Copyright (C) 2017 fido.
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
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.common.FpsLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.HelpLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.VisInfoLayer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;
import cz.cvut.fel.aic.geographtools.util.Utils2D;
import javax.vecmath.Point2d;

public class DefaultVisioInitializer implements VisioInitializer{
	
	private final PedestrianNetwork pedestrianNetwork;
	private final BikewayNetwork bikewayNetwork;
	private final HighwayNetwork highwayNetwork;
	private final TramwayNetwork tramwayNetwork;
	private final MetrowayNetwork metrowayNetwork;
	private final RailwayNetwork railwayNetwork;
    private final SimulationControlLayer simulationControlLayer;
    protected final GridLayer gridLayer;

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

    protected void initEntityLayers(Simulation simulation) { }

    protected void initLayersAfterEntityLayers() {}

    protected void initLayersBeforeEntityLayers() {}

    protected void initInfoLayers() {
        VisManager.registerLayer(HelpLayer.create());
        VisManager.registerLayer(FpsLayer.create());
        VisManager.registerLayer(VisInfoLayer.create());
        VisManager.registerLayer(simulationControlLayer);
    }

    private void initWindow() {
        final int windowHight = 1000;
        final int windowWidth = 1900;
        
        VisManager.setInvertYAxis(true);
        VisManager.setInitParam("Agentpolis operator", windowWidth, windowHight);

        VisManager.setSceneParam(new VisManager.SceneParams() {

            @Override
            public double getDefaultZoomFactor() {
                return (double) 1900 / Utils2D.getGraphWidth(highwayNetwork.getNetwork());
            }

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
