package cz.cvut.fel.aic.agentpolis.simulator.creator.initializator;

import java.util.List;

import com.google.inject.Injector;

import cz.cvut.fel.aic.agentpolis.simmodel.Agent;

/**
 * 
 * Each factory, which initializes agents, should implement this interface.
 * 
 * @author Zbynek Moler
 * 
 */
public interface AgentInitFactory {

    public List<Agent> initAllAgentLifeCycles(Injector injector);

}
