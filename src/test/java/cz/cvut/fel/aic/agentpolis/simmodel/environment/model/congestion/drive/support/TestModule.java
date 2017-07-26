/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.StandardAgentPolisModule;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;

/**
 *
 * @author fido
 */
public class TestModule extends StandardAgentPolisModule{

    public TestModule() {
        super();
        if(System.getProperty("test") == null){
            config.showVisio = false;
        }
    }
    
    

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }
    
}
