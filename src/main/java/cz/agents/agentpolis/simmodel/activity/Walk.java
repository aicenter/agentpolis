/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.activityFactory.PedestrianMoveActivityFactory;
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simmodel.eventType.DriveEvent;
import cz.agents.agentpolis.simmodel.eventType.Transit;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

/**
 * @param <A>
 * @author schaemar
 */
public class Walk<A extends Agent & MovingAgent> extends Activity<A> {

    private final Trip<Node> trip;

    private final PedestrianMoveActivityFactory moveActivityFactory;

    private final Graph<SimulationNode, SimulationEdge> graph;

    private final EventProcessor eventProcessor;

    private final TimeProvider timeProvider;

    private final int tripId;


    private Node from;

    private Node to;

    public Walk(ActivityInitializer activityInitializer, TransportNetworks transportNetworks,
                PedestrianMoveActivityFactory moveActivityFactory, TypedSimulation eventProcessor, TimeProvider timeProvider,
                A agent, Trip<Node> trip,
                int tripId) {
        super(activityInitializer, agent);
        this.trip = trip;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripId = tripId;
        graph = transportNetworks.getGraph(EGraphType.PEDESTRIAN);
    }


    @Override
    protected void performAction() {
        from = trip.getAndRemoveFirstLocation();
        move();

    }

    @Override
    protected void onChildActivityFinish(Activity activity) {
        if (trip.isEmpty()) {
            finish();
        } else {
            from = to;
            move();
        }
    }

    private void move() {
        to = trip.getAndRemoveFirstLocation();
        SimulationEdge edge = graph.getEdge(from.id, to.id);

        runChildActivity(moveActivityFactory.create(agent, edge, from, to));
        triggerVehicleEnteredEdgeEvent();
    }

    private void triggerVehicleEnteredEdgeEvent() {
        SimulationEdge edge = graph.getEdge(from.id, to.id);
        Transit transit = new Transit(timeProvider.getCurrentSimTime(), edge.wayID, tripId);
        eventProcessor.addEvent(DriveEvent.PEDESTRIAN_ENTERED_EDGE, null, null, transit);
    }
}
