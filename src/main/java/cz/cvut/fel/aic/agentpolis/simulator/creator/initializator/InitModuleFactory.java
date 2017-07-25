package cz.cvut.fel.aic.agentpolis.simulator.creator.initializator;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

/**
 * 
 * Each factory, which needs to add new Guice module, should implement this
 * interface.
 * 
 * @author Zbynek Moler
 * 
 */
public interface InitModuleFactory {

    public AbstractModule injectModule(Injector injector);

}
