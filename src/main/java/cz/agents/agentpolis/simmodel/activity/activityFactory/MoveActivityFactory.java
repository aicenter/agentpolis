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
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
@Singleton
public class MoveActivityFactory extends ActivityFactory{
    private final TransportNetworks transportNetworks;
    private final TypedSimulation eventProcessor;

    @Inject
    public MoveActivityFactory(TransportNetworks transportNetworks, TypedSimulation eventProcessor) {
        this.transportNetworks = transportNetworks;
        this.eventProcessor = eventProcessor;
    }



    public <AG extends Agent & MovingAgent> Move<AG> create(AG agent, Node from, Node to){
        return new Move<>(activityInitializer, transportNetworks, eventProcessor, agent, from, to);
    }
}
