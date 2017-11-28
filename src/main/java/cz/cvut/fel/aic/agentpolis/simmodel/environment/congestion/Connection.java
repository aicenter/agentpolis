/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEndData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEventData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleTransferData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author fido
 */
public class Connection extends EventHandlerAdapter {

    protected final SimulationProvider simulationProvider;
    protected final CongestionModel congestionModel;

    protected final SimulationNode node;
    protected boolean isTicking;

    private List<CongestionLane> inCongestionLanes = new LinkedList<>();
    private Link outLink;
    VehicleEventData vehicleEventData;

    protected long closestCarArrivalTime = Long.MAX_VALUE;
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
    void startDriving(VehicleTripData vehicleData) {
        //Log.info(this, "START Driving: " + vehicleData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLocation);
        Link nextLink = getNextLink(nextConnection);

        nextLink.startDriving(vehicleData);
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


    void scheduleVehicleTransfer(VehicleTripData vehicleTripData, CongestionLane from, CongestionLane to) {
        //Log.info(this, "Scheduling vehicle transfer START {0}, car: " + vehicleTripData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
        long delay = computeTransferDelay(vehicleTripData, to);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleTransferData(from, to, vehicleTripData, transferFinishTime);


        /* next que capacity reservation */
        to.prepareAddingToqueue(vehicleTripData);

        if (vehicleTripData.getTrip().isEmpty()) {
            if (to.parentLink.toNode == vehicleTripData.getGoal()) {
                vehicleTripData.setTripFinished(true);
            }else{
                Log.warn(this, "Incorrect ending" + vehicleTripData.getVehicle().getId(), congestionModel.timeProvider.getCurrentSimTime());
            }
        }

        congestionModel.makeScheduledEvent(this, this, delay);
        //Log.info(this, "Scheduling vehicle transfer END");
    }

    void scheduleEndDriving(VehicleTripData vehicleTripData, CongestionLane congestionLane) {
        //Log.info(this, "Schedule end driving START");

        long delay = congestionModel.computeArrivalDelay(vehicleTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleEndData(congestionLane, vehicleTripData, transferFinishTime, congestionLane.getId());

        congestionModel.makeScheduledEvent(this, this, delay);
        //Log.info(this, "Schedule end driving END");

    }

    /**
     * Tries to transfer one vehicle. The vehicle will be transfered in next tick.
     */
    protected void serveLanes(Event event) {
        //int startIndex = getNextIndex();
        for (CongestionLane l : inCongestionLanes) {
            tryTransferVehicle(l,event);
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
        VehicleTripData vehicleTripData = inCongestionLane.getFirstWaitingVehicle();

        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            scheduleEndDriving(vehicleTripData, inCongestionLane);
            return;
        }

        CongestionLane nextLane = getNextLane(inCongestionLane, vehicleTripData);

        // succesfull transfer
        if (nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            scheduleVehicleTransfer(vehicleTripData, inCongestionLane, nextLane);
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
        transferVehicle(vehicleTransferData.vehicleTripData, vehicleTransferData.from, vehicleTransferData.to);
        //Log.info(this, "Transfering vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: " + vehicleTransferData.transferFinishTime);
        vehicleEventData = null;
    }

    private void transferVehicle(VehicleTripData vehicleData, CongestionLane currentCongestionLane, CongestionLane nextCongestionLane) {
        currentCongestionLane.removeFromQueue(vehicleData);
        nextCongestionLane.addToQue(vehicleData);
        if (!vehicleData.getTrip().isEmpty()) {
                vehicleData.getTrip().removeFirstLocation();
        }
    }

    private void endDriving(VehicleTripData vehicleTripData, CongestionLane congestionLane) {
        congestionLane.removeFromQueue(vehicleTripData);
        vehicleTripData.getVehicle().setPosition(node);

        // todo - remove typing
        ((Agent) vehicleTripData.getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));
    }

    CongestionLane getNextLane(CongestionLane congestionLane, VehicleTripData vehicleTripData) {
        Link nextLink = getNextLink(congestionLane);

        Trip<SimulationNode> trip = vehicleTripData.getTrip();

        if (trip.isEmpty()) {
            return nextLink.getCongestionLaneForTripEnd();
        } else if(trip.getLocations().size() == 1){
            return nextLink.getCongestionLaneForTripEnd();
        } else {
            SimulationNode NextNextLocation = trip.getFirstLocation();
            return nextLink.getBestLaneByNextNode(NextNextLocation);
        }
    }

    protected long computeTransferDelay(VehicleTripData vehicleTripData, CongestionLane to) {
        return computeConnectionArrivalDelay(vehicleTripData);// + congestionModel.computeTransferDelay(vehicleTripData, to);
    }

    protected long computeConnectionArrivalDelay(VehicleTripData vehicleTripData) {
        DelayData delayData = vehicleTripData.getVehicle().getDelayData();
        long currentSimTime = congestionModel.timeProvider.getCurrentSimTime();
        long arrivalExpectedTime = delayData.getDelayStartTime() + delayData.getDelay();
        return Math.max(0, arrivalExpectedTime - currentSimTime);

    }

    private void endVehicleFromLastTick() {
        VehicleEndData vehicleEndData = (VehicleEndData) vehicleEventData;
        endDriving(vehicleEndData.vehicleTripData, vehicleEndData.congestionLane);
        //Log.info(this, "Ending vehicle "+vehicleEndData.vehicleTripData +"from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: ");
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
