package cz.agents.agentpolis.simmodel.environment;

import java.util.Map;

import com.google.inject.Injector;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.alite.simulation.Simulation;
import cz.agents.basestructures.Graph;

/**
 * 
 * Each factory which will do the initialization of a simulation environment
 * 
 * @author Zbynek Moler
 * 
 */
public interface EnvironmentFactory {

    public Injector injectEnvironment(Injector injector, Simulation simulation, long seed,
            Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType, Map<Integer, SimulationNode> nodesFromAllGraphs);

}
