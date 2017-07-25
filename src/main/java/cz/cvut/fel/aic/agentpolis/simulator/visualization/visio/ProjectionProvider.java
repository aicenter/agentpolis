/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author fido
 */
@Singleton
public class ProjectionProvider {
    private Projection projection;

    
    
    
    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    
    
    
    @Inject
    public ProjectionProvider() {
    }
    
    
}
