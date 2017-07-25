/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.driving;

/**
 *
 * @author fido
 */
public class DelayData {
    private final Long delay;
    
    private final Long delayStartTime;

    
    
    
    public Long getDelay() {
        return delay;
    }

    public Long getDelayStartTime() {
        return delayStartTime;
    }
    
    

    
    public DelayData(Long delay, Long delayStartTime) {
        this.delay = delay;
        this.delayStartTime = delayStartTime;
    }
    
    
}
