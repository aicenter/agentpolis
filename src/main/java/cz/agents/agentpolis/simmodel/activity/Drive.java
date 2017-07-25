/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.ActivityInitializer;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.activityFactory.VehicleMoveActivityFactory;
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
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
 * @author fido
 */
public class Drive<A extends Agent & Driver> extends Activity<A> {

    private final Vehicle vehicle;

    private final Trip<SimulationNode> trip;

    private final VehicleMoveActivityFactory moveActivityFactory;

    private final Graph<SimulationNode, SimulationEdge> graph;

    private final EventProcessor eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final int tripId;


    private SimulationNode from;

    private SimulationNode to;

    public Drive(ActivityInitializer activityInitializer, TransportNetworks transportNetworks,
                 VehicleMoveActivityFactory moveActivityFactory, TypedSimulation eventProcessor, StandardTimeProvider timeProvider,
                 A agent, Vehicle vehicle, Trip<SimulationNode> trip,
                 int tripId) {
        super(activityInitializer, agent);
        this.vehicle = vehicle;
        this.trip = trip;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.tripId = tripId;
        graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
    }


    @Override
    protected void performAction() {
        agent.startDriving(vehicle);
//        double moveTime = computeMoveTime();
        from = trip.getAndRemoveFirstLocation();
        move();

    }

    @Override
    protected void onChildActivityFinish(Activity activity) {
        if (trip.isEmpty()) {
            agent.endDriving();
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
        eventProcessor.addEvent(DriveEvent.VEHICLE_ENTERED_EDGE, null, null, transit);
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }


    /**
     * Computes time based on vehicle length and velocity
     *
     * @return
     */
//    private long computeMoveTime() {
//        double vehicleVelocityInMeterPerMillis = agent.getVelocity() / 1000;
//        long moveTime = (long) (vehicle.getLength() / vehicleVelocityInMeterPerMillis);
//
//        return MoveTimeNormalizer.normalizeMoveTimeForQueue(moveTime);
//    }


}
