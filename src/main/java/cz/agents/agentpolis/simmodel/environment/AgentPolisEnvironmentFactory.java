package cz.agents.agentpolis.simmodel.environment;

import java.util.Map;
import java.util.Random;

import java.time.ZonedDateTime;

import com.google.inject.Injector;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.alite.simulation.Simulation;
import cz.agents.basestructures.Graph;

/**
 * 
 * The factory initializes AgentPolis environment
 * 
 * @author Zbynek Moler
 * 
 */
public class AgentPolisEnvironmentFactory implements EnvironmentFactory {

    private final ZonedDateTime initDate;

    public AgentPolisEnvironmentFactory(ZonedDateTime initDate) {
        super();
        this.initDate = initDate;
    }

    @Override
    public Injector injectEnvironment(Injector injector, Simulation simulation, long seed,
            Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphByGraphType,
            Map<Integer, SimulationNode> nodesFromAllGraphs) {

        injector.createChildInjector(new AgentPolisEnvironmentModule(new Random(seed), initDate));

        return injector;
    }

}
