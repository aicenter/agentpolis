/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.MoveUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.ConnectionEvent;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.LinkedList;

/**
 * @author fido
 */
public class CongestionLane extends EventHandlerAdapter {
    private final long id;

    private static final int MIN_LINK_CAPACITY_IN_METERS = 5;
    private final double linkCapacityInMeters;

    private final TimeProvider timeProvider;
    private final SimulationProvider simulationProvider;

    public final Link parentLink;

    public final LinkedList<CongestionQueueData> drivingQueue;
    private final LinkedList<CongestionQueueData> waitingQueue;
    private LinkedList<CongestedTripData> startHereQueue;


    private double currentlyUsedCapacityInMeters;
    private double waitingQueueInMeters;

    private boolean wakePreviousConnectionAfterTransfer;
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

    /**
     * @return parent Lane {@link Lane}
     */
    public Lane getLane() {
        return this.parentLink.edge.getLaneById(this.id);
    }

    /**
     * Lane id
     *
     * @return id of lane
     */
    public long getId() {
        return this.id;
    }

    /**
     * Used space in lane
     *
     * @return meters
     */
    public double getUsedLaneCapacityInMeters() {
        return currentlyUsedCapacityInMeters;
    }

    /**
     * Waiting queue in meters
     *
     * @return meters
     */
    public double getQueueLength() {
        return waitingQueueInMeters;
    }

    /**
     * Number of cars
     *
     * @return int
     */
    public int getDrivingCarsCountOnLane() {
        return drivingQueue.size();
    }

    public boolean hasWaitingVehicles() {
        updateWaitingQueue();
        return !waitingQueue.isEmpty();
    }

    /**
     * Vehicle in waiting queue
     *
     * @return data
     */
    public CongestedTripData getFirstWaitingVehicle() {
        return waitingQueue.getFirst().getCongestedTripData();
    }


    //
    // ========================= Drive ======================
    //
    public void startDriving(CongestedTripData congestedTripData) {
        /*addToStartHereQueue*/
        if (startHereQueue == null) {
            startHereQueue = new LinkedList<>();
        }
        startHereQueue.add(congestedTripData);

        if (!eventScheduled) {
            tryScheduleStartVehicle();
        }
    }

    public long computeDelay(PhysicalVehicle vehicle, double distance) {
        double speed = computeSpeed(vehicle);
        double duration = distance / speed;
        return Math.max(1, (long) (1000 * duration));
    }

    //
    // ========================= Event simulation ======================
    //
    @Override
    public void handleEvent(Event event) {
        startFirstVehicleInStartHereQueue();
        tryScheduleStartVehicle();
    }

    public boolean wakeConnectionAfterTransfer() {
        return wakePreviousConnectionAfterTransfer;
    }

    public void setWakePreviousConnectionAfterTransfer(boolean wakePreviousConnectionAfterTransfer) {
        this.wakePreviousConnectionAfterTransfer = wakePreviousConnectionAfterTransfer;
    }


    //
    // ========================= Queue operation ======================
    //
    public boolean queueHasSpaceForVehicle(PhysicalVehicle vehicle) {
        double freeCapacity = linkCapacityInMeters - currentlyUsedCapacityInMeters;
        return freeCapacity >= vehicle.getLength();
    }

    public void removeFromWaitingQueue(CongestedTripData congestedTripData) {
        currentlyUsedCapacityInMeters -= congestedTripData.getVehicle().getLength();
        waitingQueueInMeters -= congestedTripData.getVehicle().getLength();
        waitingQueue.remove();

        updateVehiclesInQueue(congestedTripData.getVehicle().getLength());

        // wake previous connection and start que processing
        if (wakePreviousConnectionAfterTransfer) {

            /* wake up previous connection */
            wakeUpPreviousConnection(0);

            /* wake up start here processing */
            if (!eventScheduled) {
                tryScheduleStartVehicle();
            }

            setWakePreviousConnectionAfterTransfer(false);
        }
    }

    public void prepareAddingToWaitingQueue(CongestedTripData congestedTripData) {
        currentlyUsedCapacityInMeters += congestedTripData.getVehicle().getLength();
    }

    public void addToQue(CongestedTripData congestedTripData) {

        // wake up next connection
        if (isEmpty()) {
            long minDelay = computeDelay(congestedTripData.getVehicle());
            wakeUpNextConnection(minDelay);
        }

        long estimatedDelayToQueueEnd = parentLink.congestionModel.computeDelayAndSetVehicleData(congestedTripData, this);

        long minExitTime = timeProvider.getCurrentSimTime() + estimatedDelayToQueueEnd;
        drivingQueue.add(new CongestionQueueData(congestedTripData, minExitTime));
    }

    private boolean isEmpty() {
        return drivingQueue.isEmpty() && waitingQueue.isEmpty();
    }

    private void updateWaitingQueue() {
        long currentTime = timeProvider.getCurrentSimTime();
        while (!drivingQueue.isEmpty() && currentTime >= drivingQueue.peek().getMinPollTime()) {
            CongestionQueueData congestionQueueData = drivingQueue.pollFirst();
            CongestedTripData congestedTripData = congestionQueueData.getCongestedTripData();
            waitingQueue.addLast(congestionQueueData);
            waitingQueueInMeters += congestedTripData.getVehicle().getLength();
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

    private double calculateSpeedCoefficient(double carsPerKilometer) {
        //        WoframAlpha LinearModelFit[{{20, 100}, {30, 60}, {40, 40}, {70, 10}}, {x, x^2}, x]
        // interpolate speed for freeFlowSpeed = 100kmph
        //        0.0428177 x^2 - 5.61878 x + 193.757 (quadratic)
        double reducedSpeed = 0.0428177 * carsPerKilometer * carsPerKilometer - 5.61878 * carsPerKilometer + 193.757;
        return reducedSpeed / 100.0;
    }

    private void updateVehiclesInQueue(double transferredVehicleLength) {
        for (CongestionQueueData congestionQueueData : waitingQueue) {
            updateVehicle(congestionQueueData, transferredVehicleLength);
        }

        for (CongestionQueueData congestionQueueData : drivingQueue) {
            updateVehicle(congestionQueueData, transferredVehicleLength);
        }
    }

    private void updateVehicle(CongestionQueueData congestionQueueData, double transferredVehicleLength) {
        PhysicalVehicle vehicle = congestionQueueData.getCongestedTripData().getVehicle();
        CongestionModel congestionModel = parentLink.congestionModel;

        // set que before vehicle
        vehicle.setQueueBeforeVehicleLength(vehicle.getQueueBeforeVehicleLength() - transferredVehicleLength);

        long currentSimulationTime = congestionModel.getTimeProvider().getCurrentSimTime();

        // create new delay
        updateDelayData(vehicle);

        /* update min exit time for driving queue */
        congestionQueueData.setMinPollTime(currentSimulationTime + vehicle.getDelayData().getDelay());
    }

    private void startFirstVehicleInStartHereQueue() {
        addToQue(startHereQueue.pollFirst());
        eventScheduled = false;
    }

    private void tryScheduleStartVehicle() {
        if (!eventScheduled) {
            if (startHereQueue == null || startHereQueue.isEmpty()) {
                return;
            }

            CongestedTripData congestedTripData = startHereQueue.peek();

            if (!queueHasSpaceForVehicle(congestedTripData.getVehicle())) {
                setWakePreviousConnectionAfterTransfer(true);
                return;
            }

            /* next que capacity reservation */
            this.prepareAddingToWaitingQueue(congestedTripData);

            long delay = CongestionModel.computeFreeflowTransferDelay(congestedTripData.getVehicle());

            String message = "Vehicle " + congestedTripData.getVehicle().getId() + " delayed start";

            simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, message, delay);
            eventScheduled = true;
        }
    }

    private void wakeUpNextConnection(long delay) {
        parentLink.congestionModel.wakeUpConnection(parentLink.toConnection, delay);
    }

    private void wakeUpPreviousConnection(long delay) {
        parentLink.congestionModel.wakeUpConnection(parentLink.fromConnection, delay);
    }

}
