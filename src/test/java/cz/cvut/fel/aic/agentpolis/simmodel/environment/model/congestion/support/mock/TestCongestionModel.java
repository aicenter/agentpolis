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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Connection;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.ModelConstructionFailedException;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import java.security.ProviderException;

/**
 * @author fido
 */
@Singleton
public class TestCongestionModel extends CongestionModel {

	@Inject
	public TestCongestionModel(
			TransportNetworks transportNetworks, 
			AgentpolisConfig config,
			SimulationProvider simulationProvider, 
			TimeProvider timeProvider, 
			ShapeUtils shapeUtils,
			MoveUtil moveUtil
	) throws ModelConstructionFailedException,
			ProviderException {
		super(transportNetworks, config, simulationProvider, timeProvider, shapeUtils, moveUtil);
	}

	public Connection getConnectionByNode(SimulationNode node) {
		return connectionsMappedByNodes.get(node);
	}

	public Link getLinkByEdge(SimulationEdge edge) {
		return linksMappedByEdges.get(edge);
	}

}
