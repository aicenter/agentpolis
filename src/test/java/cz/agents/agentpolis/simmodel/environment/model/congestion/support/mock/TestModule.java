/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion.support.mock;

import com.google.inject.AbstractModule;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;

/**
 *
 * @author fido
 */
public class TestModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(TestTimeProvider.class);
        bind(PositionUtil.class).to(TestPositionUtil.class);
    }
    
}
