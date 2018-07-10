package cz.cvut.fel.aic.agentpolis.simpresentationlayer.support;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;

public class TestModule extends StandardAgentPolisModule {

    public TestModule() {
        super();

//        if(System.getProperty("test") == null){
//            agentpolisConfig.showVisio = false;
//        }

        agentpolisConfig.showVisio = true; //VisualTests.SHOW_VISIO;

        agentpolisConfig.congestionModel.batchSize = 1;
        agentpolisConfig.congestionModel.maxFlowPerLane = 5.0;
        agentpolisConfig.congestionModel.defaultCrossroadDrivingLanes = 2;
    }



    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(TestVisioInitializer.class);
    }

}
