package cz.cvut.fel.aic.agentpolis.simulator.creator.initializator;

import com.google.inject.AbstractModule;

/**
 * 
 *
 *@author Marek Cuchy
 *
 */
public final class EmptyModule extends AbstractModule {

    private static EmptyModule instance;

    private EmptyModule() {
    }

    public static EmptyModule getInstance() {
        if (null == instance) {
            instance = new EmptyModule();
        }
        return instance;
    }
    
    @Override
    protected void configure() {
    }
}
