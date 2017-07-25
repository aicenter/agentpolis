/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;


import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.TimeConsumingActivity;
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.agentpolis.simmodel.environment.model.action.moving.MoveUtil;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Node;
import cz.agents.multimodalstructures.edges.RoadEdge;

import java.util.logging.Level;

/**
 * @param <A>
 * @author fido
 */
public class Move<A extends Agent & MovingAgent> extends TimeConsumingActivity<A> {

    protected final EventProcessor eventProcessor;
    protected final SimulationEdge edge;
    protected final SimulationNode from;
    protected final SimulationNode to;


    public Move(ActivityInitializer activityInitializer,
                TypedSimulation eventProcessor, A agent, SimulationEdge edge, SimulationNode from, SimulationNode to) {
        super(activityInitializer, agent);
        this.eventProcessor = eventProcessor;
        this.edge = edge;
        this.from = from;
        this.to = to;
    }


    @Override
    protected void performAction() {
        agent.setTargetNode(null);
        agent.setPosition(to);
        finish();
    }

    /**
     * The method ensure that the agent movement is feasible for execution. If
     * the movement is not feasible then agent will freeze.
     *
     * @param edge
     * @return
     */
    protected boolean checkFeasibility(Edge edge) {
        return edge != null;

    }

    @Override
    protected long performPreDelayActions() {
        long duration = 0;
        if (checkFeasibility(edge)) {

            agent.setTargetNode(to);
            double velocity = MoveUtil.computeAgentOnEdgeVelocity(agent.getVelocity(), edge.allowedMaxSpeedInMpS);
            duration = MoveUtil.computeDuration(velocity, edge.length);

            agent.setDelayData(new DelayData(duration, eventProcessor.getCurrentTime()));
        } else {
            Log.log(this, Level.SEVERE, "The agent with id: {0} is not able to execute movement. Agent will freeze "
                            + "on the current position. It does not exist the edge from {1} to {2}", agent.getId(), edge.fromId,
                    edge.toId);
            fail("It does not exist the edge from: " + edge.fromId + " to: " + edge.toId);
        }
        return duration;
    }


}
