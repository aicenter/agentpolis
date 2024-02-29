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
package cz.cvut.fel.aic.agentpolis.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.DateTimeParser;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

/**
 *
 * @author david
 */
@Singleton
public class SimulationUtils {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SimulationUtils.class);
	
	private final AgentpolisConfig config;

	private final long simulationDuration;



	public long getSimulationDuration(){
		return simulationDuration;
	}


	
	@Inject
	public SimulationUtils(AgentpolisConfig config) {
		this.config = config;

		if(config.endTime.isEmpty()) {
			simulationDuration = (config.simulationDuration.seconds + config.simulationDuration.minutes * 60
				+ config.simulationDuration.hours * 60 * 60 + config.simulationDuration.days * 60 * 60 * 24) * 1000;
		}
		else{
			if(config.simulationDuration != null){
				LOGGER.warn("Both end_time and simulation_duration are set. Using end_time.");
			}
			ZonedDateTime endTime = DateTimeParser.parseDateTime(config.endTime);
			ZonedDateTime startTime = DateTimeParser.parseDateTime(config.startTime);
			simulationDuration = endTime.toInstant().toEpochMilli() - startTime.toInstant().toEpochMilli();
		}
	}
}
