/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import com.google.inject.AbstractModule;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;

/**
 *
 * @author fido
 */
public class TestModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(TestTimeProvider.class);
    }
    
}
