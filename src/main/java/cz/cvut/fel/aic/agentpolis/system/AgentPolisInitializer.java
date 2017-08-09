/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.system;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author fido
 */
public class AgentPolisInitializer {
    private static final Logger LOGGER = Logger.getLogger(AgentPolisInitializer.class);
    
    
    
    
    private Module mainModule;
    
    private final List<Object> loggers = new ArrayList<>();
    
    private final Config configuration;

    
    
    
    public AgentPolisInitializer(StandardAgentPolisModule mainModule) {
        this.mainModule = mainModule;
        configuration = mainModule.getConfig();
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
    
    /**
    * Add your own logger which processes the incoming the implementations of {@code LogItem}. These
    * implementations of
    * {@code LogItem} should be subscribed by logger via {@code Subscribe} annotation
    *
    * @param logger
    */
    public void addLogger(Object logger) {

        if (isSubscribeAnnotationIncluded(logger)) {
            loggers.add(logger);
        } else {
            LOGGER.info("The logger [" + logger + "] was skipped because it does not cointains Subscribe annotation");
        }

    }
    
    
    
    private boolean isSubscribeAnnotationIncluded(Object logger) {
        for (Method method : logger.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                return true;
            }
        }
        return false;
    }
}
