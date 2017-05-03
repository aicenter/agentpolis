/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.alite.common.event.typed.TypedSimulation;

/**
 *
 * @author fido
 */
@Singleton
public class ActivityInitializer {
    
    private final TypedSimulation simulation;

    public TypedSimulation getSimulation() {
        return simulation;
    }
    
    

    @Inject
    public ActivityInitializer(TypedSimulation simulation) {
        this.simulation = simulation;
    }
    
    
}
