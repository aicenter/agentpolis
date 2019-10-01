/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity;


import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.TimeConsumingActivity;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.geographtools.Edge;


import org.slf4j.LoggerFactory;

/**
 * @param <A>
 * @author fido
 */
public class Move<A extends Agent & MovingAgent> extends TimeConsumingActivity<A> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Move.class);
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
		agent.setDelayData(null);
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
			duration = MoveUtil.computeDuration(agent, edge);
			agent.setDelayData(new DelayData(duration, eventProcessor.getCurrentTime(), edge.getLengthCm()));
		} else {
			LOGGER.error("The agent with id: {} is not able to execute movement. Agent will freeze "
							+ "on the current position. It does not exist the edge from {} to {}", new Object[]{ 
							agent.getId(), edge.fromNode.getId(), edge.toNode.getId()});
			fail("It does not exist the edge from: " + edge.fromNode.getId() + " to: " + edge.toNode.getId());
		}
		return duration;
	}


}
