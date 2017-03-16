/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.activityFactory.MoveActivityFactory;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Node;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.eventType.DriveEvent;
import cz.agents.agentpolis.simmodel.eventType.Transit;
import cz.agents.alite.common.event.EventProcessor;
import cz.agents.basestructures.Edge;
import cz.agents.basestructures.Graph;

/**
 *
 * @author fido
 * @param <A>
 */
public class Drive<A extends Agent & TransportAgent> extends Activity<A>{
    
    private final Vehicle vehicle;
    
    private final Trip<Node> trip;
    
    private final MoveActivityFactory moveActivityFactory;
    
    private final Graph<SimulationNode, SimulationEdge> graph;
    
    private final EventProcessor eventProcessor;
    
    private final TimeProvider timeProvider;
    
    
    private Node from;
    
    private Node to;

    public Drive(TransportNetworks transportNetworks, MoveActivityFactory moveActivityFactory, 
            EventProcessor eventProcessor, TimeProvider timeProvider, A agent, Vehicle vehicle, Trip<Node> trip) {
        super(agent);
        this.vehicle = vehicle;
        this.trip = trip;
        this.moveActivityFactory = moveActivityFactory;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
    }




    @Override
    protected void performAction() {
//        double moveTime = computeMoveTime();
        from = trip.getAndRemoveFirstLocation();
        move();
        
    }

    @Override
    protected void onChildActivityFinish(Activity activity) {
        for (AgentPolisEntity entiy : agent.getCargo()) {
            entiy.setPosition(agent.getPosition());
        }
        if(trip.isEmpty()){
            finish();
        }
        else{
            from = to;
            move();
        }
    }

    private void move() {
        to = trip.getAndRemoveFirstLocation();
        runChildActivity(moveActivityFactory.create(agent, from, to));
        triggerVehicleEnteredEdgeEvent();
    }

    private void triggerVehicleEnteredEdgeEvent() {
        SimulationEdge edge = graph.getEdge(from.id, to.id);
        Transit transit = new Transit(timeProvider.getCurrentSimTime(), edge.wayID);
        eventProcessor.addEvent(DriveEvent.VEHICLE_ENTERED_EDGE, null, null, transit);
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
