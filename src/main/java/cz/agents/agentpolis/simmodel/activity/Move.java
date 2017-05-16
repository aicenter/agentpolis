/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;


import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;

import java.util.logging.Level;

/**
 * @param <A>
 * @author fido
 */
public class Move<A extends Agent & MovingAgent> extends Activity<A> {

    protected final EventProcessor eventProcessor;
    protected final Edge edge;
    protected final Node from;
    protected final Node to;


    public Move(ActivityInitializer activityInitializer,
                TypedSimulation eventProcessor, A agent, Edge edge, Node from, Node to) {
        super(activityInitializer, agent);
        this.eventProcessor = eventProcessor;
        this.edge = edge;
        this.from = from;
        this.to = to;
    }


    @Override
    protected void performAction() {
        if (checkFeasibility(edge)) {

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
        } else {
            Log.log(this, Level.SEVERE, "The agent with id: {0} is not able to execute movement. Agent will freeze "
                            + "on the current position. It does not exist the edge from {1} to {2}", agent.getId(), edge.fromId,
                    edge.toId);
            fail("It does not exist the edge from: " + edge.fromId + " to: " + edge.toId);
        }
    }

    /**
     * The method ensure that the agent movement is feasible for execution. If
     * the movement is not feasible then agent will freeze.
     */
    protected boolean checkFeasibility(Edge edge) {
        return edge != null;

    }

    protected void finishMove() {
        agent.setTargetNode(null);
        agent.setPosition(to);
        finish();
    }


}
