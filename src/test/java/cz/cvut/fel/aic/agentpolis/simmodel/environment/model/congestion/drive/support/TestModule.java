/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
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
        
        config.congestionModel.batchSize = 1;
        config.congestionModel.maxFlowPerLane = 5;
        config.congestionModel.defaultCrossroadDrivingLanes = 2;
    }
    
    

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }
    
}
