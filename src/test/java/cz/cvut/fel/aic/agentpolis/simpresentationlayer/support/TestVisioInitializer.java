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
package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.*;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.KeyToggleLayer;
import java.awt.*;

@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer {

	protected final NodeIdLayer nodeIdLayer;
	protected final HighwayLayer highwayLayer;
	private final VisLayer backgroundLayer;
	private final MapTilesLayer mapTilesLayer;

	@Inject
	public TestVisioInitializer(Simulation simulation, PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork,
								HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork,
								RailwayNetwork railwayNetwork, NodeIdLayer nodeIdLayer, HighwayLayer highwayLayer,
								SimulationControlLayer simulationControlLayer, GridLayer gridLayer, MapTilesLayer mapTiles, AgentpolisConfig config) {
		super(simulation, highwayNetwork,
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
