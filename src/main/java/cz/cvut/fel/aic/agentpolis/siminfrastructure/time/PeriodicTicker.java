/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import cz.agents.alite.common.event.EventType;

/**
 *
 * @author fido
 */
public abstract class PeriodicTicker extends EventHandlerAdapter{
    private final SimulationProvider simulationProvider;
    
    private final int tickLength;
    
    private final EventType eventType;

    
    
    
    public PeriodicTicker(SimulationProvider simulationProvider, int tickLength, EventType eventType) {
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
