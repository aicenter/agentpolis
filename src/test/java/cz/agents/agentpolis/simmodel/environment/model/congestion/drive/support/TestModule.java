/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.drive.support;

import cz.agents.agentpolis.simmodel.environment.StandardAgentPolisModule;
import cz.agents.agentpolis.simulator.visualization.visio.VisioInitializer;

/**
 *
 * @author fido
 */
public class TestModule extends StandardAgentPolisModule{

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }
    
}
