package cz.agents.agentpolis.simulator.creator.initializator;

import com.google.inject.Injector;

/**
 * 
 * Each factory, which initializes something before simulation run and needs
 * injector, should implement this interface.
 * 
 * @author Zbynek Moler
 * 
 */
public interface InitFactory {

    public void initRestEnvironment(Injector injector);

}
