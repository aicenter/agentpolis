/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.eventType;

/**
 *
 * @author fido
 */
public class Transit {
    private final long time;
    
    private final long osmId;

    public long getTime() {
        return time;
    }

    public long getId() {
        return osmId;
    }

    
    
    
    public Transit(long time, long osmId) {
        this.time = time;
        this.osmId = osmId;
    }
    
    
}
