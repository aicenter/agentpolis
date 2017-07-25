/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Wait;

/**
 *
 * @author fido
 */
@Singleton
public class WaitActivityFactory extends ActivityFactory{
    
    
    public <A extends Agent> void runActivity(A agent, long waitTime) {
        create(agent, waitTime).run();
    }
    
    public <A extends Agent> Wait create(A agent, long waitTime) {
        return new Wait<>(activityInitializer, agent, waitTime);
    }
}
