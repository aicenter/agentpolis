/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.geographtools.GPSLocation;

import java.util.LinkedList;

/**
 * @author fido
 */
public class Lane {

    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;


    private final double linkCapacityInMeters;

    private final LinkedList<VehicleQueueData> drivingQueue;
    private final LinkedList<VehicleQueueData> waitingQueue;

    final Link link;

    private final TimeProvider timeProvider;


    private LinkedList<VehicleTripData> startHereQueue;

    private Link nextLink;

    private double currentlyUsedCapacityInMeters;
    
    private double waitingQueueInMeters;
    
    private boolean wakeConnectionAfterTransfer;

    public boolean wakeConnectionAfterTransfer() {
        return wakeConnectionAfterTransfer;
    }

    public void setWakeConnectionAfterTransfer(boolean wakeConnectionAfterTransfer) {
        this.wakeConnectionAfterTransfer = wakeConnectionAfterTransfer;
    }
    
    


    public Link getNextLink() {
        return nextLink;
    }


    public Lane(Link link, double linkCapacityInMeters, TimeProvider timeProvider) {
        this.link = link;
        this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS
                ? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
        this.timeProvider = timeProvider;
        this.drivingQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
    }


    void removeFromTop(VehicleTripData vehicleData) {
        currentlyUsedCapacityInMeters -= vehicleData.getVehicle().getLength();
        waitingQueueInMeters -= vehicleData.getVehicle().getLength();
        waitingQueue.remove();
        
        updateVehiclesInQueue(vehicleData.getVehicle().getLength());
    }

    VehicleTripData getFirstWaitingVehicle() {
        return waitingQueue.getFirst().getVehicleTripData();
    }

    private boolean isEmpty() {
        return drivingQueue.isEmpty() && waitingQueue.isEmpty();
    }

    boolean hasWaitingVehicles() {
        updateWaitingQueue();
        return !waitingQueue.isEmpty();

    }

    private void updateWaitingQueue() {
        long currentTime = timeProvider.getCurrentSimTime();
        while (!drivingQueue.isEmpty() && currentTime >= drivingQueue.peek().getMinPollTime()) {
            VehicleQueueData vehicleQueueData = drivingQueue.pollFirst();
            VehicleTripData vehicleTripData = vehicleQueueData.getVehicleTripData();
            waitingQueue.addLast(vehicleQueueData);
            waitingQueueInMeters += vehicleTripData.getVehicle().getLength();

        }
    }

    void startDriving(VehicleTripData vehicleTripData, long delay) {
        if (queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            addToQue(vehicleTripData, delay);
        } else {
            addToStartHereQueue(vehicleTripData);
        }
    }

    void tryToServeStartFromHereQueue() {
        if (startHereQueue == null) return;
        while (!startHereQueue.isEmpty() && queueHasSpaceForVehicle(startHereQueue.peekFirst().getVehicle())) {
            VehicleTripData vehicleData = startHereQueue.pollFirst();
            long delay = computeDelay(vehicleData.getVehicle());

            // for visio
            Driver driver = vehicleData.getVehicle().getDriver();
            driver.setTargetNode(link.toNode);
            vehicleData.getVehicle().setPosition(link.fromNode);
            driver.setDelayData(new DelayData(delay, timeProvider.getCurrentSimTime()));
            addToQue(vehicleData, delay);
        }
    }

    void addToQue(VehicleTripData vehicleTripData, long delay) {

        long minExitTime = timeProvider.getCurrentSimTime() + delay;

//        // for visio
//        Driver driver =  vehicleTripData.getVehicle().getDriver();
//        driver.setTargetNode(link.toNode);
//        vehicleTripData.getVehicle().setPosition(link.fromNode);
//        driver.setDelayData(new DelayData(delay, timeProvider.getCurrentSimTime()));
        currentlyUsedCapacityInMeters += vehicleTripData.getVehicle().getLength();
        drivingQueue.add(new VehicleQueueData(vehicleTripData, minExitTime));
    }

    boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
        double freeCapacity = linkCapacityInMeters - currentlyUsedCapacityInMeters;
        return freeCapacity > vehicle.getLength();
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
    
    long computeDelay(PhysicalVehicle vehicle) {
        CongestionModel congestionModel = link.congestionModel;
        SimulationEdge edge = link.edge;
        double freeFlowVelocity = MoveUtil.computeAgentOnEdgeVelocity(vehicle.getVelocity(), 
                edge.allowedMaxSpeedInMpS);
        double capacity = edge.getLanesCount() * edge.length;
        double level = currentlyUsedCapacityInMeters / capacity;

        double speed = freeFlowVelocity * interpolateSquared(1, 0.1, 1 - level);
        double distance = congestionModel.positionUtil.getDistance(
                vehicle.getPrecisePosition(), congestionModel.graph.getNode(edge.toId))
                - vehicle.getQueueBeforeVehicleLength();
        double duration = distance / speed;
        long durationInMs = Math.max(1, (long) (1000 * duration));
        return durationInMs;
    }
    
    private double interpolateSquared(double from, double to, double x) {
        double v = x * x;
        double y = (from * v) + (to * (1 - v));
        if (y < Math.min(from, to) || y > Math.max(from, to))
            Log.error(this, y + ": value out of range (" + from + "," + to + ")!");
        return y;
    }

    private void updateVehiclesInQueue(double transferredVehicleLength) {
        for (VehicleQueueData vehicleQueueData : waitingQueue) {
            updateVehicle(vehicleQueueData, transferredVehicleLength);
        }
        
        for (VehicleQueueData vehicleQueueData : drivingQueue) {
            updateVehicle(vehicleQueueData, transferredVehicleLength);
        }
    }

    private void updateVehicle(VehicleQueueData vehicleQueueData, double transferredVehicleLength) {
        PhysicalVehicle vehicle = vehicleQueueData.getVehicleTripData().getVehicle();
        CongestionModel congestionModel = link.congestionModel;
        
        // set que before vehicle
        vehicle.setQueueBeforeVehicleLength(vehicle.getQueueBeforeVehicleLength() - transferredVehicleLength);
        
        // change position to current interpolated position
        GPSLocation currentInterpolatedLocation = link.congestionModel.getPositionInterpolatedForVehicle(vehicle);
        vehicle.setPrecisePosition(currentInterpolatedLocation);
        
        // create new delay
        long delay = computeDelay(vehicle);
        vehicle.getDriver().setDelayData(new DelayData(delay, congestionModel.getTimeProvider().getCurrentSimTime()));
    }
}
