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
package cz.cvut.fel.aic.agentpolis.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;

/**
 *
 * @author david
 */
@Singleton
public class SimulationUtils {
	
	private final AgentpolisConfig config;

	
	@Inject
	public SimulationUtils(AgentpolisConfig config) {
		this.config = config;
	}
	
	
	
	public long computeSimulationDuration(){
		long simulationDuration = config.simulationDuration.seconds + config.simulationDuration.minutes * 60 
				+ config.simulationDuration.hours * 60 * 60 + config.simulationDuration.days * 60 * 60 * 24;
		simulationDuration *= 1000;
		return simulationDuration;
	}
}
