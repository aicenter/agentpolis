/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.time;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;

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
