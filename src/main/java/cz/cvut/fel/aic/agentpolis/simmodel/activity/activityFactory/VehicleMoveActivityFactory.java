/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.Move;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.VehicleMove;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

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


    public <AG extends Agent & Driver> Move<AG> create(AG agent, SimulationEdge edge, SimulationNode from, SimulationNode to) {
        return new VehicleMove<>(activityInitializer,
                eventProcessor, agent, edge, from, to);
    }
}
