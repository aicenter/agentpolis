/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

import com.google.inject.Inject;

/**
 *
 * @author fido
 */

public abstract class ActivityFactory {
    
    protected ActivityInitializer activityInitializer;
    
    @Inject
    public void init(ActivityInitializer activityInitializer){
        this.activityInitializer = activityInitializer;
    }
    
    
}
