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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.CongestionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.Link;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.CongestionMessage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.VehicleEndData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.VehicleEventData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.support.VehicleTransferData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes.CongestionLane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class Connection extends EventHandlerAdapter {

    protected final SimulationProvider simulationProvider;
    protected final CongestionModel congestionModel;

    protected final SimulationNode node;
    protected boolean isTicking;

    private List<CongestionLane> inCongestionLanes = new LinkedList<>();
    private Link outLink;
    public VehicleEventData vehicleEventData;

    private long closestCarArrivalTime = Long.MAX_VALUE;
    private int lastIndexOfLaneList = Integer.MAX_VALUE - 1; // there is not going to be a connection with Integer.Max_value lanes.


    public Connection(SimulationProvider simulationProvider, AgentpolisConfig config, CongestionModel congestionModel,
                      SimulationNode node) {
        this.congestionModel = congestionModel;
        this.simulationProvider = simulationProvider;
        this.node = node;
        this.vehicleEventData = null;
        isTicking = false;
    }

    //
    // ========================= Build ======================
    //
    public Link getNextLink(Connection nextConnection) {
        return outLink;
    }

    public Link getNextLink(CongestionLane inputCongestionLane) {
        return outLink;
    }

    public void setOutLink(Link outLink, CongestionLane inCongestionLane) {
        this.outLink = outLink;
        this.inCongestionLanes.add(inCongestionLane);
    }


    //
    // ========================= Drive ======================
    //
    public void startDriving(CongestedTripData congestedTripData) {
        //Log.info(this, "START Driving: " + vehicleData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
        Trip<SimulationNode> trip = congestedTripData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLocation);
        Link nextLink = getNextLink(nextConnection);

        nextLink.startDriving(congestedTripData);
    }

    //
    // ========================= Simulation event transfer ======================
    //
    @Override
    public void handleEvent(Event event) {
        /* if event comes while connection is woken up */
        if (vehicleEventData != null && vehicleEventData.transferFinishTime > congestionModel.timeProvider.getCurrentSimTime()) {
            /* To early to process vehicle event*/
            //Log.info(this, "if event comes while connection is woken up");
            return;
        }
        /* if the vehicle should be transfer by this event */
        if (vehicleEventData != null) {
            if (vehicleEventData instanceof VehicleTransferData) {
                transferVehicleFromLastTick();
            } else {
                endVehicleFromLastTick();
            }
        }
        // Wait and make tries
        serveLanes(event);
    }


    void scheduleVehicleTransfer(CongestedTripData congestedTripData, CongestionLane from, CongestionLane to) {
        //Log.info(this, "Scheduling vehicle transfer START {0}, car: " + congestedTripData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
        long delay = computeTransferDelay(congestedTripData, to);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleTransferData(from, to, congestedTripData, transferFinishTime);


        /* next que capacity reservation */
        to.prepareAddingToWaitingQueue(congestedTripData);

        if (congestedTripData.getTrip().isEmpty()) {
            if (to.parentLink.toNode == congestedTripData.getGoal()) {
                congestedTripData.setTripFinished(true);
            } else {
                Log.warn(this, "Incorrect ending" + congestedTripData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
            }
        }

        congestionModel.makeScheduledEvent(this, this, delay);
        //Log.info(this, "Scheduling vehicle transfer END");
    }

    void scheduleEndDriving(CongestedTripData congestedTripData, CongestionLane congestionLane) {
        //Log.info(this, "Schedule end driving START");
        long delay = congestionModel.computeArrivalDelay(congestedTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleEndData(congestionLane, congestedTripData, transferFinishTime, congestionLane.getId());
        congestionModel.makeScheduledEvent(this, this, delay);
        //Log.info(this, "Schedule end driving END");

    }

    /**
     * Tries to transfer one vehicle. The vehicle will be transfered in next tick.
     */
    protected void serveLanes(Event event) {
        int startIndex = getNextIndex();
        for (int i = 0; i < inCongestionLanes.size(); i++) {
            tryTransferVehicle(inCongestionLanes.get((startIndex + i) % inCongestionLanes.size()), event);
        }
    }

    private void tryTransferVehicle(CongestionLane inCongestionLane, Event event) {
        //Log.info(this, "TryTransferVehicles START in lane:" + inCongestionLane.getLane().getLaneUniqueId());
        /* no one is waiting */
        if (!inCongestionLane.hasWaitingVehicles()) {
            checkDrivingQue(inCongestionLane);
            return;
        }

        // first vehicle
        CongestedTripData congestedTripData = inCongestionLane.getFirstWaitingVehicle();

        // vehicle ends on this node
        if (congestedTripData.isTripFinished()) {
            scheduleEndDriving(congestedTripData, inCongestionLane);
            return;
        }

        CongestionLane nextLane = getNextLane(inCongestionLane, congestedTripData);

        // succesfull transfer
        if (nextLane.queueHasSpaceForVehicle(congestedTripData.getVehicle())) {
            scheduleVehicleTransfer(congestedTripData, inCongestionLane, nextLane);
        }
        // next queue is full
        else {
            Log.log(Connection.class, Level.FINE, "Connection {0}: No space in queue to {1}!", node.id, nextLane.parentLink.toNode.id);
            nextLane.setWakePreviousConnectionAfterTransfer(true);
        }

    }

    //
    // ========================= Vehicle transfer ======================
    //
    private void transferVehicleFromLastTick() {
        VehicleTransferData vehicleTransferData = (VehicleTransferData) vehicleEventData;
        transferVehicle(vehicleTransferData.congestedTripData, vehicleTransferData.from, vehicleTransferData.to);
        //Log.info(this, "Transfering vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: " + vehicleTransferData.transferFinishTime);
        vehicleEventData = null;
    }

    private void transferVehicle(CongestedTripData vehicleData, CongestionLane currentCongestionLane, CongestionLane nextCongestionLane) {
        currentCongestionLane.removeFromWaitingQueue(vehicleData);
        nextCongestionLane.addToQue(vehicleData);
        if (!vehicleData.getTrip().isEmpty()) {
            vehicleData.getTrip().removeFirstLocation();
        }
    }

    private void endDriving(CongestedTripData vehicleTripData, CongestionLane congestionLane) {
        congestionLane.removeFromWaitingQueue(vehicleTripData);
        vehicleTripData.getVehicle().setPosition(node);

        // todo - remove typing
        ((Agent) vehicleTripData.getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));
    }

    CongestionLane getNextLane(CongestionLane congestionLane, CongestedTripData vehicleTripData) {
        Link nextLink = getNextLink(congestionLane);

        Trip<SimulationNode> trip = vehicleTripData.getTrip();

        if (trip.isEmpty()) {
            return nextLink.getCongestionLaneForTripEnd();
        } else if (trip.getLocations().size() == 1) {
            return nextLink.getCongestionLaneForTripEnd();
        } else {
            SimulationNode NextNextLocation = trip.getFirstLocation();
            return nextLink.getBestLaneByNextNode(NextNextLocation);
        }
    }

    protected long computeTransferDelay(CongestedTripData vehicleTripData, CongestionLane to) {
        return computeConnectionArrivalDelay(vehicleTripData);// + congestionModel.computeTransferDelay(congestedTripData, to);
    }

    protected long computeConnectionArrivalDelay(CongestedTripData vehicleTripData) {
        DelayData delayData = vehicleTripData.getVehicle().getDelayData();
        long currentSimTime = congestionModel.timeProvider.getCurrentSimTime();
        long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
        return Math.max(0, arrivalExpectedTime - currentSimTime);

    }

    private void endVehicleFromLastTick() {
        VehicleEndData vehicleEndData = (VehicleEndData) vehicleEventData;
        endDriving(vehicleEndData.getCongestedTripData(), vehicleEndData.getCongestionLane());
        //Log.info(this, "Ending vehicle "+vehicleEndData.congestedTripData +"from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: ");
        vehicleEventData = null;
    }

    protected void checkDrivingQue(CongestionLane lane) {
        if (!lane.drivingQueue.isEmpty()) {
            long currentTime = congestionModel.timeProvider.getCurrentSimTime();
            if (currentTime >= closestCarArrivalTime) {
                closestCarArrivalTime = Long.MAX_VALUE;
            }
            Log.info(this, "checking driving queue of " + lane + " at " + currentTime);
            long firstTransferToWaitingQueueTime = lane.drivingQueue.peek().getMinPollTime();
            if (firstTransferToWaitingQueueTime < closestCarArrivalTime) {
                closestCarArrivalTime = firstTransferToWaitingQueueTime;
                congestionModel.makeTickEvent(this, this, firstTransferToWaitingQueueTime - congestionModel.timeProvider.getCurrentSimTime());
            }
        }
    }

    private int getNextIndex() {
        if (lastIndexOfLaneList >= inCongestionLanes.size() - 1) lastIndexOfLaneList = 0;
        else lastIndexOfLaneList++;
        return this.lastIndexOfLaneList;
    }
}