package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.agentdrive.support;

import com.google.inject.AbstractModule;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.TestPositionUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion.support.mock.TestTimeProvider;
import cz.cvut.fel.aic.agentpolis.simpresentationlayer.support.TestVisioInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisMainModule;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

/**
 * @author fido
 */
public class TestModule extends StandardAgentPolisModule {

    public TestModule() {
        super();

        agentpolisConfig.showVisio = true; //VisualTests.SHOW_VISIO;
    }

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(TestTimeProvider.class);
        bind(PositionUtil.class).to(TestPositionUtil.class);
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }


}
