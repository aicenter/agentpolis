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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author fido
 */
public class CongestionLane extends EventHandlerAdapter {
    private final long id;

    private final TimeProvider timeProvider;
    private final SimulationProvider simulationProvider;

    final Link parentLink; // parent parentLink

    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;
    private final double linkCapacityInMeters;

    final LinkedList<VehicleQueueData> drivingQueue;
    private final LinkedList<VehicleQueueData> waitingQueue;
    private LinkedList<VehicleTripData> startHereQueue;


    private double currentlyUsedCapacityInMeters;
    private double waitingQueueInMeters;

    private boolean wakeConnectionAfterTransfer;

    private boolean eventScheduled;

    public CongestionLane(Link link, long laneId, double linkCapacityInMeters, TimeProvider timeProvider, SimulationProvider simulationProvider) {
        this.id = laneId;
        this.parentLink = link;
        this.linkCapacityInMeters = linkCapacityInMeters > MIN_LINK_CAPACITY_IN_METERS ? linkCapacityInMeters : MIN_LINK_CAPACITY_IN_METERS;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        this.drivingQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
        eventScheduled = false;
    }

    @Override
    public void handleEvent(Event event) {
        startFirstVehicleInStartHereQueue();
        tryScheduleStartVehicle();
    }

    /**
     * Information about lane
     *
     * @return
     */
    public Lane getLane() {
        return this.parentLink.edge.getLaneById(this.id);
    }

    public double getUsedLaneCapacityInMeters() {
        return currentlyUsedCapacityInMeters;
    }

    /**
     * Connection wakeup
     */
    public boolean wakeConnectionAfterTransfer() {
        return wakeConnectionAfterTransfer;
    }

    public double getQueueLength() {
        return waitingQueueInMeters;
    }

    void setWakeConnectionAfterTransfer(boolean wakeConnectionAfterTransfer) {
        this.wakeConnectionAfterTransfer = wakeConnectionAfterTransfer;
    }

    void startDriving(VehicleTripData vehicleTripData) {
        addToStartHereQueue(vehicleTripData);
        handleChange();
    }

    boolean hasWaitingVehicles() {
        updateWaitingQueue();
        return !waitingQueue.isEmpty();

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

    void prepareAddingToqueue(VehicleTripData vehicleTripData) {
        currentlyUsedCapacityInMeters += vehicleTripData.getVehicle().getLength();
    }

    void addToQue(VehicleTripData vehicleTripData) {

        // wake up next connection
        if (isEmpty()) {
            long minDelay = computeDelay(vehicleTripData.getVehicle());
            wakeUpNextConnection(minDelay);
        }

        long estimatedDelayToQueueEnd = parentLink.congestionModel.computeDelayAndSetVehicleData(vehicleTripData, this);

        long minExitTime = timeProvider.getCurrentSimTime() + estimatedDelayToQueueEnd;
        drivingQueue.add(new VehicleQueueData(vehicleTripData, minExitTime));
    }

    // TODO: Check next link for space - problem with long vehicle (above MIN_LINK_LENGTH)
    boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
        double freeCapacity = linkCapacityInMeters - currentlyUsedCapacityInMeters;
        return freeCapacity >= vehicle.getLength();
    }

    long computeDelay(PhysicalVehicle vehicle, double distance) {
        double speed = computeSpeed(vehicle);
        double duration = distance / speed;
        return Math.max(1, (long) (1000 * duration));
    }

    private boolean isEmpty() {
        return drivingQueue.isEmpty() && waitingQueue.isEmpty();
    }

    private void addToStartHereQueue(VehicleTripData vehicleTripData) {
        if (startHereQueue == null) {
            startHereQueue = new LinkedList<>();
        }
        startHereQueue.add(vehicleTripData);
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

    private long computeDelay(PhysicalVehicle vehicle) {
        SimulationEdge edge = parentLink.edge;
        return computeDelay(vehicle, edge.shape.getShapeLength());
    }

    private void updateDelayData(PhysicalVehicle vehicle) {
        CongestionModel congestionModel = parentLink.congestionModel;
        long currentSimTime = congestionModel.getTimeProvider().getCurrentSimTime();

        SimulationEdge edge = parentLink.edge;
        DelayData previousDelayData = vehicle.getDelayData();
        double completionRatioOfPreviousDelay = (currentSimTime - previousDelayData.getDelayStartTime()) / (double) previousDelayData.getDelay();
        if (completionRatioOfPreviousDelay > 1.0) completionRatioOfPreviousDelay = 1.0;

        double startDistanceOffset = previousDelayData.getStartDistanceOffset() + completionRatioOfPreviousDelay * previousDelayData.getDelayDistance();

        double distance = edge.shape.getShapeLength() - startDistanceOffset - vehicle.getQueueBeforeVehicleLength();
        assert distance >= 0.0;

        long durationInMs = computeDelay(vehicle, distance);
        vehicle.getDriver().setDelayData(new DelayData(durationInMs, currentSimTime, distance, startDistanceOffset));
    }

    private double computeSpeed(PhysicalVehicle vehicle) {
        SimulationEdge edge = parentLink.edge;
        double freeFlowVelocity = MoveUtil.computeAgentOnEdgeVelocity(vehicle.getVelocity(),
                edge.allowedMaxSpeedInMpS);

        double speed = freeFlowVelocity;
        if (parentLink.congestionModel.addFundamentalDiagramDelay) {
            speed = computeCongestedSpeed(freeFlowVelocity, edge);
        }

        return speed;
    }

    private double computeCongestedSpeed(double freeFlowVelocity, SimulationEdge edge) {
        double carsPerKilometer = getDrivingCarsCountOnLane() / edge.shape.getShapeLength() * 1000.0;

        double congestedSpeed;
        if (carsPerKilometer < 20) {
            congestedSpeed = freeFlowVelocity;
        } else if (carsPerKilometer > 70) {
            congestedSpeed = 0.1 * freeFlowVelocity;
        } else {
            congestedSpeed = freeFlowVelocity * calculateSpeedCoefficient(carsPerKilometer);
        }
        Log.info(this, "Congested speed: " + carsPerKilometer + "cars / km -> " + congestedSpeed + "m / s");

        return congestedSpeed;
    }

    private int getDrivingCarsCountOnLane() {
        return drivingQueue.size();
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
        CongestionModel congestionModel = parentLink.congestionModel;

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

        long delay = CongestionModel.computeFreeflowTransferDelay(vehicleTripData.getVehicle());

        String message = "Vehicle " + vehicleTripData.getVehicle().getId() + " delayed start";

        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, message, delay);
        eventScheduled = true;
    }

    private void wakeUpNextConnection(long delay) {
        parentLink.congestionModel.wakeUpConnection(parentLink.toConnection, delay);
    }

    private void wakeUpPreviousConnection(long delay) {
        parentLink.congestionModel.wakeUpConnection(parentLink.fromConnection, delay);
    }

}
