/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.common.FpsLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.HelpLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.VisInfoLayer;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.Utils2D;
import java.util.function.ToDoubleFunction;
import javax.vecmath.Point2d;

public class DefaultVisioInitializer implements VisioInitializer {

	private final Simulation simulation;
	private final HighwayNetwork highwayNetwork;
	private final SimulationControlLayer simulationControlLayer;
	protected final GridLayer gridLayer;

	@Inject
	public DefaultVisioInitializer(Simulation simulation, HighwayNetwork highwayNetwork, SimulationControlLayer simulationControlLayer, GridLayer gridLayer) {
		this.simulation = simulation;
		this.highwayNetwork = highwayNetwork;
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
		VisManager.registerLayer(simulationControlLayer);
	}

	private void initWindow() {
		final int windowHeight = 1000;
		final int windowWidth = 1900;

		VisManager.setInvertYAxis(true);
		VisManager.setInitParam("Agentpolis operator", windowWidth, windowHeight);

		VisManager.setSceneParam(new VisManager.SceneParams() {

			@Override
			public double getDefaultZoomFactor() {
				return (double) 1900 / Utils2D.getGraphWidth(highwayNetwork.getNetwork());
			}

			@Override
			public Point2d getDefaultLookAt() {
				double centroidLat = getCentroidCoordinate(GPSLocation::getLatitude);
				double centroidLon = getCentroidCoordinate(GPSLocation::getLongitude);
				GPSLocation projectedCentroid = VisioPositionUtil.toMercatorProjection(centroidLat, centroidLon);
				return new Point2d(projectedCentroid.getLongitudeProjected(), projectedCentroid.getLatitudeProjected());
			}

			private double getCentroidCoordinate(ToDoubleFunction<SimulationNode> coordinateMapper) {
				return highwayNetwork.getNetwork().getAllNodes().stream().mapToDouble(coordinateMapper).average().orElse(0);
			}
		});

		VisManager.init(simulation);
	}

}
