/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.TimeConsumingActivity;

/**
 *
 * @author fido
 * @param <A>
 */

public class Wait<A extends Agent> extends TimeConsumingActivity<A>{
    
    private final long waitTime;

    
    public Wait(ActivityInitializer activityInitializer, A agent, long waitTime) {
        super(activityInitializer, agent);
        this.waitTime = waitTime;
    }

    @Override
    protected long performPreDelayActions() {
        return waitTime;
    }

    @Override
    protected void performAction() {
        finish();
    }
    
}
