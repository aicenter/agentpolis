package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

public class TestModule extends StandardAgentPolisModule {

    public TestModule() {
        super();

        agentpolisConfig.visio.showVisio = true; //VisualTests.SHOW_VISIO;
        agentpolisConfig.srid = testSRID();
    }

    static int testSRID() {
        return 32650; // SRID used for both tests - changing this should give various distance results, but shouldn't break the visualization
    }


    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }

}
