package cz.cvut.fel.aic.agentpolis.system;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class AgentPolisInitializer {
    private Module mainModule;

    public AgentPolisInitializer(StandardAgentPolisModule mainModule) {
        this.mainModule = mainModule;
    }
    
    public AgentPolisInitializer() {
        this(new StandardAgentPolisModule());
    }

    public void overrideModule(Module module){
        mainModule = Modules.override(mainModule).with(module);
    }
    
    public Injector initialize(){
        Injector injector = Guice.createInjector(mainModule);
        return injector;
    }
}
