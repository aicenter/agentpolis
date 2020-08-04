/*
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support.CarLayer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;

import java.awt.*;

/**
 * @author fido
 */
@Singleton
public class TestVisioInitializer extends DefaultVisioInitializer {

	protected final LayerManagementLayer layerManagementLayer;

	private final HighwayLayer highwayLayer;

	private final NodeIdLayer nodeIdLayer;

	private final CarLayer carLayer;


	@Inject
	public TestVisioInitializer(Simulation simulation, HighwayNetwork highwayNetwork, LayerManagementLayer layerManagementLayer,
								SimulationControlLayer simulationControlLayer, HighwayLayer highwayLayer,
								NodeIdLayer nodeIdLayer, GridLayer gridLayer, CarLayer carLayer) {
		super(simulation, highwayNetwork, simulationControlLayer, gridLayer);
		this.layerManagementLayer = layerManagementLayer;
		this.highwayLayer = highwayLayer;
		this.nodeIdLayer = nodeIdLayer;
		this.carLayer = carLayer;
	}

	@Override
	protected void initGraphLayers() {
		VisManager.registerLayer(ColorLayer.create(Color.white));
		super.initGraphLayers();
		VisManager.registerLayer(layerManagementLayer.createManageableLayer("Road network", highwayLayer));
	}

	@Override
	protected void initEntityLayers(Simulation simulation) {
//		super.initEntityLayers(simulation, projection); 
		VisManager.registerLayer(layerManagementLayer.createManageableLayer("Cars", carLayer));
	}

	@Override
	protected void initLayersAfterEntityLayers() {
		VisManager.registerLayer(layerManagementLayer.createManageableLayer("Node ids", nodeIdLayer));
		VisManager.registerLayer(layerManagementLayer);
	}


}
