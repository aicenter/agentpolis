/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.eventType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author fido
 */

@Singleton
public class TripIdGenerator {
    
    private int currentId;

    
    
    @Inject
    public TripIdGenerator() {
        this.currentId = 0;
    }
        

    public int getId(){
        int id = currentId;
        currentId++;
        return id;
    }
}
