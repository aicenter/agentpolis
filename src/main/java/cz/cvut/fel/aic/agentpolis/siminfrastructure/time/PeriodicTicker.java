/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

/**
 *
 * @author fido
 */
public abstract class PeriodicTicker extends EventHandlerAdapter{
    private final SimulationProvider simulationProvider;
    
    private final int tickLength;
    
    private final Enum eventType;

    
    
    
    public PeriodicTicker(SimulationProvider simulationProvider, int tickLength, Enum eventType) {
        this.simulationProvider = simulationProvider;
        this.tickLength = tickLength;
        this.eventType = eventType;
    }

    

    
    public void start(){
        simulationProvider.getSimulation().addEvent(eventType, this, null, null, tickLength);
    }

    @Override
    public void handleEvent(Event event) {
        handleTick();
        simulationProvider.getSimulation().addEvent(eventType, this, null, null, tickLength);
    }

    protected abstract void handleTick();
}
