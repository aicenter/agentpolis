/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.LinkedList;
import java.util.logging.Level;

/**
 * @author fido
 */
public class Lane extends EventHandlerAdapter {

    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;


    private final double linkCapacityInMeters;

    final LinkedList<VehicleQueueData> drivingQueue;
    private final LinkedList<VehicleQueueData> waitingQueue;

    final Link link;

    private final TimeProvider timeProvider;


    private LinkedList<VehicleTripData> startHereQueue;

    private Link nextLink;

    private double currentlyUsedCapacityInMeters;

    private double waitingQueueInMeters;

    private boolean wakeConnectionAfterTransfer;

    private final SimulationProvider simulationProvider;


    private boolean eventScheduled;
    private CongestionModel congestionModel;
    private double criticalDensity;


    public boolean wakeConnectionAfterTransfer() {
        return wakeConnectionAfterTransfer;
    }

    public void setWakeConnectionAfterTransfer(boolean wakeConnectionAfterTransfer) {
        this.wakeConnectionAfterTransfer = wakeConnectionAfterTransfer;
    }

    public Link getNextLink() {
        return nextLink;
    }


    public Lane(Link link, double linkCapacityInMeters, TimeProvider timeProvider,
                SimulationProvider simulationProvider, double criticalDensity) {
        this.link = link;
        this.congestionModel = link.congestionModel;
        this.simulationProvider = simulationProvider;
        this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS
                ? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
        this.timeProvider = timeProvider;
        this.drivingQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
        eventScheduled = false;
        this.criticalDensity = criticalDensity;
    }


    void removeFromQueue(VehicleTripData vehicleData) {
        currentlyUsedCapacityInMeters -= vehicleData.getVehicle().getLength();
        waitingQueueInMeters -= vehicleData.getVehicle().getLength();
        waitingQueue.remove();

        updateVehiclesInQueue(vehicleData.getVehicle().getLength());

        // wake previous connection and start que processing
        if (wakeConnectionAfterTransfer) {
            
            /* wake up previous connection */
            wakeUpPreviousConnection(0);
            
            /* wake up start here processing */
            handleChange();

            setWakeConnectionAfterTransfer(false);
        }
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

    void startDriving(VehicleTripData vehicleTripData) {
        addToStartHereQueue(vehicleTripData);
        handleChange();
    }

    void prepareAddingToqueue(VehicleTripData vehicleTripData) {
        currentlyUsedCapacityInMeters += vehicleTripData.getVehicle().getLength();
    }

    void addToQue(VehicleTripData vehicleTripData) {

        // wake up next connection
        if (isEmpty()) {
            long minDelay = computeDelay(vehicleTripData.getVehicle());
            wakeUpNextConnection(minDelay);
        }

        long estimatedDelayToQueueEnd = link.congestionModel.computeDelayAndSetVehicleData(vehicleTripData, this);

        long minExitTime = timeProvider.getCurrentSimTime() + estimatedDelayToQueueEnd;
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

    long computeDelay(PhysicalVehicle vehicle, double distance) {
        double speed = computeSpeed(vehicle);
        double duration = distance / speed;
        long durationInMs = Math.max(1, (long) (1000 * duration));
        return durationInMs;
    }

    long computeDelay(PhysicalVehicle vehicle) {
        SimulationEdge edge = link.edge;
        return computeDelay(vehicle, edge.shape.getShapeLength());
    }

    void updateDelayData(PhysicalVehicle vehicle) {
        CongestionModel congestionModel = link.congestionModel;
        long currentSimTime = congestionModel.getTimeProvider().getCurrentSimTime();

        SimulationEdge edge = link.edge;
        DelayData previousDelayData = vehicle.getDelayData();
        double completionRatioOfPreviousDelay = (currentSimTime - previousDelayData.getDelayStartTime()) / (double) previousDelayData.getDelay();
        if (completionRatioOfPreviousDelay > 1.0) completionRatioOfPreviousDelay = 1.0;

        double startDistanceOffset = previousDelayData.getStartDistanceOffset() + completionRatioOfPreviousDelay * previousDelayData.getDelayDistance();

        double distance = edge.shape.getShapeLength() - startDistanceOffset - vehicle.getQueueBeforeVehicleLength();
        assert distance >= 0.0;

        long durationInMs = computeDelay(vehicle, distance);
        vehicle.getDriver().setDelayData(new DelayData(durationInMs, currentSimTime, distance, startDistanceOffset));
    }

    double computeSpeed(PhysicalVehicle vehicle) {
        SimulationEdge edge = link.edge;
        double freeFlowVelocity = MoveUtil.computeAgentOnEdgeVelocity(vehicle.getVelocity(),
                edge.allowedMaxSpeedInMpS);

        double speed = freeFlowVelocity;
        if (link.congestionModel.addFundamentalDiagramDelay) {
            speed = computeCongestedSpeed(freeFlowVelocity, edge);
        }

        return speed;
    }

    private double computeCongestedSpeed(double freeFlowVelocity, SimulationEdge edge) {
        double carsPerKilometer = getDrivingCarsCountOnLane() / edge.shape.getShapeLength() * 1000.0;

        double congestedSpeed;
        if (carsPerKilometer <= criticalDensity) {
            congestedSpeed = freeFlowVelocity;
        } else if (carsPerKilometer > 70) {
            congestedSpeed = 0.1 * freeFlowVelocity;
        } else {
            congestedSpeed = freeFlowVelocity * calculateSpeedCoefficient(carsPerKilometer);
        }
        Log.log(this, Level.FINE, "Congested speed: " + carsPerKilometer + "cars / km -> " + congestedSpeed + "m / s");

        return congestedSpeed;
    }

    public int getDrivingCarsCountOnLane() {
        return drivingQueue.size() + waitingQueue.size();
    }

    private double calculateSpeedCoefficient(double carsPerKilometer) {
        //        WoframAlpha LinearModelFit[{{20, 100}, {30, 60}, {40, 40}, {70, 10}}, {x, x^2}, x]
        // interpolate speed for freeFlowSpeed = 100kmph
        //        0.0428177 x^2 - 5.61878 x + 193.757 (quadratic)
        double x = carsPerKilometer;
        double reducedSpeed = (0.0428177 * x * x - 5.61878 * x + 193.757);
        return reducedSpeed / 100.0;
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

        long currentSimulationTime = congestionModel.getTimeProvider().getCurrentSimTime();

        // create new delay
        updateDelayData(vehicle);

        /* update min exit time for driving queue */
        vehicleQueueData.setMinPollTime(currentSimulationTime + vehicle.getDelayData().getDelay());
    }

    private void handleChange() {
        if (!eventScheduled) {
            tryScheduleStartVehicle();
        }
    }

    @Override
    public void handleEvent(Event event) {
        startFirstVehicleInStartHereQueue();
        tryScheduleStartVehicle();
    }

    public void wakeUpNextConnection(long delay) {
        link.congestionModel.wakeUpConnection(link.toConnection, delay);
    }

    public void wakeUpPreviousConnection(long delay) {
        link.congestionModel.wakeUpConnection(link.fromConnection, delay);
    }

    private void startFirstVehicleInStartHereQueue() {
        VehicleTripData vehicleTripData = startHereQueue.pollFirst();
        addToQue(vehicleTripData);
        eventScheduled = false;
    }

    private void tryScheduleStartVehicle() {
        if (startHereQueue == null || startHereQueue.isEmpty()) {
            return;
        }

        VehicleTripData vehicleTripData = startHereQueue.peek();

        if (!queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            setWakeConnectionAfterTransfer(true);
            return;
        }

        /* next que capacity reservation */
        this.prepareAddingToqueue(vehicleTripData);

        long delay = congestionModel.computeTransferDelay(vehicleTripData, this);
        String message = "Vehicle " + vehicleTripData.getVehicle().getId() + " delayed start";

        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, message, (delay > 0 ? delay : 1));
        eventScheduled = true;
    }

}
