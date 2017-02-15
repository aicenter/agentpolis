/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity.activityFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.Move;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Node;

/**
 *
 * @author fido
 */
@Singleton
public class MoveActivityFactory {
    private final TransportNetworks transportNetworks;
    private final EventProcessor eventProcessor;

    @Inject
    public MoveActivityFactory(TransportNetworks transportNetworks, EventProcessor eventProcessor) {
        this.transportNetworks = transportNetworks;
        this.eventProcessor = eventProcessor;
    }



    public <AG extends Agent & TransportAgent> Move<AG> create(AG agent, Node from, Node to){
        return new Move<>(transportNetworks, eventProcessor, agent, from, to);
    }
}
