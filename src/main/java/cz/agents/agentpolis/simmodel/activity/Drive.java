/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.activity;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.Activity;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.activity.activityFactory.MoveActivityFactory;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.basestructures.Node;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;

/**
 *
 * @author fido
 * @param <A>
 */
public class Drive<A extends Agent & TransportAgent> extends Activity<A>{
    
    private final Vehicle vehicle;
    
    private final Trip<Node> trip;
    
    private final TransportNetworks transportNetworks;
    
    private final MoveActivityFactory moveActivityFactory;
    
    
    private Node from;
    
    private Node to;

    public Drive(TransportNetworks transportNetworks, MoveActivityFactory moveActivityFactory, 
            A agent, Vehicle vehicle, Trip<Node> trip) {
        super(agent);
        this.vehicle = vehicle;
        this.trip = trip;
        this.transportNetworks = transportNetworks;
        this.moveActivityFactory = moveActivityFactory;
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
