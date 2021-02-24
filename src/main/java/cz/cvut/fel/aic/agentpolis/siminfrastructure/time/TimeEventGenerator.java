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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

/**
 *
 * @author fido
 */
@Singleton
public class TimeEventGenerator extends EventHandlerAdapter{
	private final SimulationProvider simulationProvider;
	
	private static final int DEFAULT_TICK_LENTH = 40;

	@Inject
	public TimeEventGenerator(SimulationProvider simulationProvider) {
		this.simulationProvider = simulationProvider;
	}
	
	public void start(){
		simulationProvider.getSimulation().addEvent(TimeGeneratorEvent.TICK, this, null, null, DEFAULT_TICK_LENTH);
	}

	@Override
	public void handleEvent(Event event) {
		simulationProvider.getSimulation().addEvent(TimeGeneratorEvent.TICK, this, null, null, DEFAULT_TICK_LENTH);
	}
	
	
}
