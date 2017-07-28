/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.activity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.ActivityInitializer;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.PedestrianMoveActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.DriveEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.eventType.Transit;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.cvut.fel.aic.geographtools.Graph;

/**
 * @param <A>
 * @author schaemar
 */
public class Walk<A extends Agent & MovingAgent> extends Activity<A> {

    private final Trip<SimulationNode> trip;

    private final PedestrianMoveActivityFactory moveActivityFactory;

    private final Graph<SimulationNode, SimulationEdge> graph;

    private final EventProcessor eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final int tripId;


    private SimulationNode from;

    private SimulationNode to;

    public Walk(ActivityInitializer activityInitializer, TransportNetworks transportNetworks,
                PedestrianMoveActivityFactory moveActivityFactory, TypedSimulation eventProcessor, StandardTimeProvider timeProvider,
                A agent, Trip<SimulationNode> trip,
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
