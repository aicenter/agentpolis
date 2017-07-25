/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;

/**
 *
 * @author fido
 * @param <A> Agent type
 */
public abstract class TimeConsumingActivity<A extends Agent> extends Activity<A>{
    
    public TimeConsumingActivity(ActivityInitializer activityInitializer, A agent) {
        super(activityInitializer, agent);
    }

    @Override
    void runActityLogic() {
        long delay = performPreDelayActions();
        if(!failed()){
            getEventProcessor().addEvent(new EventHandler() {

                    @Override
                    public void handleEvent(Event event) {
                        performPostDelayActions();
                    }

                    @Override
                    public EventProcessor getEventProcessor() {
                        return null;
                    }

            }, delay);
        }
    }

    protected abstract long performPreDelayActions();

    private void performPostDelayActions() {
        super.runActityLogic();
    }
    
    
    
    
}
