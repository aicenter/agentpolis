/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import cz.agents.agentpolis.siminfrastructure.logger.LogItem;
import cz.agents.agentpolis.simmodel.environment.StandardAgentPolisModule;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.agentpolis.simulator.logger.subscriber.CSVLogSubscriber;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.LogItemViewer;
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
    
    private final AgentPolisConfiguration configuration;
    
    private final Set<Class<? extends LogItem>> allowedLogItemClassesLogItemViewer = new HashSet<>();
    
    private final Set<Class<? extends LogItem>> allowedLogItemClassesForCSV = new HashSet<>();
    
    
    private LogItemViewer logItemViewer;

    
    
    
    public AgentPolisInitializer(AgentPolisConfiguration configuration, StandardAgentPolisModule mainModule) {
        this.mainModule = mainModule;
        this.configuration = configuration;
        mainModule.initializeParametrs(configuration, loggers, allowedLogItemClassesLogItemViewer);
    }
    
    public AgentPolisInitializer(AgentPolisConfiguration configuration) {
        this(configuration, new StandardAgentPolisModule());
    }
    
    
    public void overrideModule(Module module){
        mainModule = Modules.override(mainModule).with(module);
    }
    
    public Injector initialize(){
        Injector injector = Guice.createInjector(mainModule);
        
        initLoggers(injector);
        
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
    
    public void addAllowEventForEventViewer(Class<? extends LogItem> allowedLogItemClass) {
        this.allowedLogItemClassesLogItemViewer.add(allowedLogItemClass);
    }

    public void addAllowEventForEventViewer(Set<Class<? extends LogItem>> allowedLogItemClasses) {
        this.allowedLogItemClassesLogItemViewer.addAll(allowedLogItemClasses);
    }
    
    public void addAllowedLogItemForCSVLogger(Class<? extends LogItem> allowedLogItemClass) {
        allowedLogItemClassesForCSV.add(allowedLogItemClass);
    }

    public void addAllowedLogItemForCSVLogger(Set<Class<? extends LogItem>> allowedLogItemClasses) {
        allowedLogItemClassesForCSV.addAll(allowedLogItemClasses);
    }
    
    
    
    private boolean isSubscribeAnnotationIncluded(Object logger) {
        for (Method method : logger.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                return true;
            }
        }
        return false;
    }

    private void initLoggers(Injector injector) {
        LOGGER.info("Initialization of logger - event bus");
        
        if (configuration.showEventViewer) {
            logItemViewer = injector.getInstance(LogItemViewer.class);
            addLogger(logItemViewer);
        }
        
        initCSV(configuration.pathToCSVEventLogFile, injector);
    }
    
    private void initCSV(String pathToCSVEventLogFile, Injector injector) {
        try {
            if (!allowedLogItemClassesForCSV.isEmpty()) {
                CSVLogSubscriber csvLogSubscriber = CSVLogSubscriber.newInstance(ImmutableSet.copyOf
                        (allowedLogItemClassesForCSV), new File(pathToCSVEventLogFile));
                addLogger(csvLogSubscriber);
                injector.getInstance(SimulationCreator.class).addSimulationFinishedListener(csvLogSubscriber);
                return;
            }
        } catch (IOException e) {
            LOGGER.warn("CSV Logger was not initialized", e);
        }
        LOGGER.warn("CSV Logger was not initialized");
    }
}
