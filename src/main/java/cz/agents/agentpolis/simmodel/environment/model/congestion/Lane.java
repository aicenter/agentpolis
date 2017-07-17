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
 * @author fido
 */
public class Lane {

    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;


    private final double linkCapacityInMeters;

    private final LinkedList<VehicleQueueData> drivingLaneQueue;
    private final LinkedList<VehicleQueueData> waitingQueue;

    final Link link;

    private final TimeProvider timeProvider;


    private LinkedList<VehicleTripData> startHereQueue;

    private Link nextLink;

    private double currentlyUsedCapacityInMeters;
    private double waitingQueueInMeters;


    public Link getNextLink() {
        return nextLink;
    }


    public Lane(Link link, double linkCapacityInMeters, TimeProvider timeProvider) {
        this.link = link;
        this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS
                ? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
        this.timeProvider = timeProvider;
        this.drivingLaneQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
    }


    void removeFromTop(VehicleTripData vehicleData) {
        currentlyUsedCapacityInMeters -= vehicleData.getVehicle().getLength();
        waitingQueueInMeters -= vehicleData.getVehicle().getLength();
        waitingQueue.remove();
    }

    VehicleTripData getFirstWaitingVehicle() {
        return waitingQueue.getFirst().getVehicleTripData();
    }

    private boolean isEmpty() {
        return drivingLaneQueue.isEmpty() && waitingQueue.isEmpty();
    }

    boolean hasWaitingVehicles() {
        updateWaitingQueue();
        return !waitingQueue.isEmpty();

    }

    private void updateWaitingQueue() {
        long currentTime = timeProvider.getCurrentSimTime();
        while (!drivingLaneQueue.isEmpty() && currentTime >= drivingLaneQueue.peek().getMinPollTime()) {
            VehicleQueueData vehicleQueueData = drivingLaneQueue.pollFirst();
            VehicleTripData vehicleTripData = vehicleQueueData.getVehicleTripData();
            waitingQueue.addLast(vehicleQueueData);
            waitingQueueInMeters += vehicleTripData.getVehicle().getLength();

        }
    }

    void startDriving(VehicleTripData vehicleTripData, long delay) {
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
        currentlyUsedCapacityInMeters += vehicleTripData.getVehicle().getLength();
        drivingLaneQueue.add(new VehicleQueueData(vehicleTripData, minExitTime));
    }

    boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
        return linkCapacityInMeters - currentlyUsedCapacityInMeters > vehicle.getLength();
    }

    private void addToStartHereQueue(VehicleTripData vehicleTripData) {
        if (startHereQueue == null) {
            startHereQueue = new LinkedList<>();
        }
        startHereQueue.add(vehicleTripData);
    }

    public double getQueueLength() {
        return waitingQueueInMeters;
    }

    public double getUsedLaneCapacityInMeters() {
        return currentlyUsedCapacityInMeters;
    }
}
