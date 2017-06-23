/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.ActivityFactory;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.Move;
import cz.agents.agentpolis.simmodel.activity.VehicleMove;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

/**
 * @author fido
 */
@Singleton
public class VehicleMoveActivityFactory extends ActivityFactory {
    private final TypedSimulation eventProcessor;

    @Inject
    public VehicleMoveActivityFactory(TypedSimulation eventProcessor) {
        this.eventProcessor = eventProcessor;
    }


    public <AG extends Agent & Driver> Move<AG> create(AG agent, Edge edge, SimulationNode from, SimulationNode to) {
        return new VehicleMove<>(activityInitializer,
                eventProcessor, agent, edge, from, to);
    }
}
