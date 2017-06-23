/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import java.util.LinkedList;

/**
 *
 * @author fido
 */
public class Lane {
    
    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;
    
    
    private final double linkCapacityInMeters;
    
    private final LinkedList<VehicleQueueData> queue;
    
    final Link link;
    
    private final TimeProvider timeProvider;
    
    
    private LinkedList<VehicleTripData> startHereQueue;
    
    private Link nextLink;

    private double currentlyUsedCapacityInMeters;

    
    public Link getNextLink() {
        return nextLink;
    }
    
    
    
    public Lane(Link link, double linkCapacityInMeters, TimeProvider timeProvider) {
        this.link = link;
        this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS 
                ? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
        this.timeProvider = timeProvider;
        this.queue = new LinkedList<>();
    }
    
    
    void removeFromTop(){
        queue.remove();
    }
    
    VehicleTripData getFirstWaitingVehicle(){
        return queue.getFirst().getVehicleTripData();
    }
    
    private boolean isEmpty(){
        return queue.isEmpty();
    }
    
    boolean hasWaitingVehicles(){
        if(isEmpty()){
            return false;
        }
        return timeProvider.getCurrentSimTime() >= queue.peek().getMinPollTime();
    }
    
    void startDriving(VehicleTripData vehicleTripData, long delay){
//        if(queueHasSpaceForVehicle(vehicleTripData.getVehicle())){
            addToQue(vehicleTripData, delay);
//        }
//        else{
//            addToStartHereQueue(vehicleTripData);
//        }
    }

    void addToQue(VehicleTripData vehicleTripData, long delay) {
        
        long minExitTime = timeProvider.getCurrentSimTime() + delay;
        
//        // for visio
//        Driver driver =  vehicleTripData.getVehicle().getDriver();
//        driver.setTargetNode(link.toNode);
//        vehicleTripData.getVehicle().setPosition(link.fromNode);
//        driver.setDelayData(new DelayData(delay, timeProvider.getCurrentSimTime()));
        
        queue.add(new VehicleQueueData(vehicleTripData, minExitTime));
    }

    boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
        return linkCapacityInMeters - currentlyUsedCapacityInMeters > vehicle.getLength();
    }

    private void addToStartHereQueue(VehicleTripData vehicleTripData) {
        if(startHereQueue == null){
            startHereQueue = new LinkedList<>();
        }
        startHereQueue.add(vehicleTripData);
    }
}
