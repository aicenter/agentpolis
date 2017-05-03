/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;


import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.util.logging.Level;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.alite.common.event.typed.TypedSimulation;

/**
 *
 * @author fido
 * @param <A>
 */
public class Move<A extends Agent & TransportAgent> extends Activity<A>{
//    
//    private final TransportNetworks transportNetworks;
    
    private final EventProcessor eventProcessor;
    
    private final Node from;
    
    private final Node to;
    
    private final Graph<?, ? extends Edge> graph;

    public Move(TransportNetworks transportNetworks, TypedSimulation eventProcessor, A agent, Node from, Node to) {
        super(agent);
        this.from = from;
        this.to = to;
//        this.transportNetworks = transportNetworks;
        this.eventProcessor = eventProcessor;
        // todo - add option to choose graph type here
        graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
    }

    @Override
    protected void performAction() {
        Edge edge = graph.getEdge(from.id, to.id);
        
        
        if(checkFeasibility(edge)){
            agent.setTargetNode(to);
            
            long duration = MoveUtil.computeDuration(agent.getVelocity(), edge.length);
            
            agent.setDelayData(new DelayData(duration, eventProcessor.getCurrentTime()));
            
            eventProcessor.addEvent(new EventHandler() {

                @Override
                public void handleEvent(Event event) {
                    finishMove();
                }

                @Override
                public EventProcessor getEventProcessor() {
                    return eventProcessor;
                }
            }, duration);
        }
        else{
            Log.log(this, Level.SEVERE, "The agent with id: {0} is not able to execute movement. Agent will freeze "
                    + "on the current possiton. It does not exist the edge from {1} to {2}", agent.getId(), from.id, 
                    to.id);
            fail("It does not exist the edge from: " + from.id + " to: " + to.id);
        }
    }
    
    /**
     * The method ensure that the agent movement is feasible for execution. If
     * the movement is not feasible then agent will freeze.
     */
    private boolean checkFeasibility(Edge edge) {
        if (edge == null) {
            return false;
        }

        return true;
    }
    
    private void finishMove(){
        agent.setTargetNode(null);
        agent.setPosition(to);
        finish();
    }
    
    

}
