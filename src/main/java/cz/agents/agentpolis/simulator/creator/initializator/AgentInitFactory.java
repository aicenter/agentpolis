package cz.agents.agentpolis.simulator.creator.initializator;

import java.util.List;

import com.google.inject.Injector;

import cz.agents.agentpolis.simmodel.Agent;

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
