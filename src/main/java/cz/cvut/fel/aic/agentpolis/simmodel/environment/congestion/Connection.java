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
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEndData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEventData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleTransferData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;

import java.util.logging.Level;

/**
 * @author fido
 */
public class Connection extends EventHandlerAdapter {

    protected final CongestionModel congestionModel;

    protected final SimulationProvider simulationProvider;

    protected final SimulationNode node;

    private Lane inLane;

    private Link outLink;

    protected boolean isTicking;

    private VehicleEventData vehicleEventData;

    protected long closestCarArrivalTime = Long.MAX_VALUE;


    public Connection(SimulationProvider simulationProvider, AgentpolisConfig config, CongestionModel congestionModel,
                      SimulationNode node) {
        this.congestionModel = congestionModel;
        this.simulationProvider = simulationProvider;
        this.node = node;
        isTicking = false;
    }


    @Override
    public void handleEvent(Event event) {

        /* if event comes while connection is woken up */
        if (vehicleEventData != null
                && vehicleEventData.transferFinishTime > congestionModel.timeProvider.getCurrentSimTime()) {
            Log.info(this, "if event comes while connection is woken up");
            return;
        }
        
        /* if the vehicle should be transfere by this event */
        if (vehicleEventData != null) {
            if (vehicleEventData instanceof VehicleTransferData) {
                transferVehicleFromLastTick();
            } else {
                endVehicleFromLastTick();
            }

        }

        serveLanes();
    }

    protected void transferVehicle(VehicleTripData vehicleData, Lane currentLane, Lane nextLane) {
        currentLane.removeFromQueue(vehicleData);

        nextLane.addToQue(vehicleData);

        if (!vehicleData.getTrip().isEmpty()) {
            vehicleData.getTrip().removeFirstLocation();
        }
    }

    /**
     * Tries to transffer one vehicle. The vehicle will be transfered in next tick.
     *
     * @return true if next vehicle can be tried, false otherwise.
     */
    private boolean tryTransferVehicle() {

        Log.info(this, "TryTransferVehicles START");
        /* no one is waiting */
        if (!inLane.hasWaitingVehicles()) {
            checkDrivingQue(inLane);
            return false;
        }

        // first vehicle
        VehicleTripData vehicleTripData = inLane.getFirstWaitingVehicle();

        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            scheduleEndDriving(vehicleTripData, inLane);
            return false;
        }

        Lane nextLane = getNextLane(inLane, vehicleTripData);

        // succesfull transfer
        if (nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            scheduleVehicleTransfer(vehicleTripData, inLane, nextLane);
            return false;
        }
        // next queue is full
        else {
            Log.log(Connection.class, Level.FINE, "Connection {0}: No space in queue to {1}!", node.id,
                    nextLane.link.toNode.id);
            nextLane.setWakeConnectionAfterTransfer(true);
            return false;
        }
    }

    void startDriving(VehicleTripData vehicleData) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();

        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLocation);
        Link nextLink = getNextLink(nextConnection);

        nextLink.startDriving(vehicleData);
    }


    protected Lane getNextLane(Lane lane, VehicleTripData vehicleTripData) {
        Link nextLink = getNextLink(lane);

        Trip<SimulationNode> trip = vehicleTripData.getTrip();

        if (trip.isEmpty()) {
            return nextLink.getLaneForTripEnd();
        } else {
            SimulationNode NextNextLocation = trip.getFirstLocation();
            return nextLink.getLaneByNextNode(NextNextLocation);
        }
    }

    protected Link getNextLink(Lane inputLane) {
        return outLink;
    }

    protected void endDriving(VehicleTripData vehicleTripData, Lane lane) {
        lane.removeFromQueue(vehicleTripData);

        vehicleTripData.getVehicle().setPosition(node);

        // todo - remove typing
        ((Agent) vehicleTripData.getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));
    }

    public Link getNextLink(Connection nextConnection) {
        return outLink;
    }


    protected void serveLanes() {
        while (tryTransferVehicle()) {
        }
    }

    void setOutLink(Link outLink, Lane inLane) {
        this.outLink = outLink;
        this.inLane = inLane;
    }

    private void transferVehicleFromLastTick() {
        VehicleTransferData vehicleTransferData = (VehicleTransferData) vehicleEventData;
        transferVehicle(vehicleTransferData.vehicleTripData, vehicleTransferData.from, vehicleTransferData.to);
        Log.info(this, "Transfering vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: " + vehicleTransferData.transferFinishTime);
        vehicleEventData = null;
    }

    void scheduleVehicleTransfer(VehicleTripData vehicleTripData, Lane from, Lane to) {
        Log.info(this, "Scheduling vehicle transfer START {0}",congestionModel.timeProvider.getCurrentSimTime() );
        long delay = computeTransferDelay(vehicleTripData, to);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleTransferData(from, to, vehicleTripData, transferFinishTime);

        /* next que capacity reservation */
        to.prepareAddingToqueue(vehicleTripData);

        congestionModel.makeScheduledEvent(this, this, delay);
        Log.info(this, "Scheduling vehicle transfer END");
    }

    protected long computeTransferDelay(VehicleTripData vehicleTripData, Lane to) {
        return congestionModel.computeTransferDelay(vehicleTripData, to);
    }


    void scheduleEndDriving(VehicleTripData vehicleTripData, Lane lane) {
        Log.info(this, "Schedule end driving START");

//        long delay = CongestionModel.computeFreeFlowTransferDelay(vehicleTripData.getVehicle());
        long delay = congestionModel.computeArrivalDelay(vehicleTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleEndData(lane, vehicleTripData, transferFinishTime);

//        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, delay);
        congestionModel.makeScheduledEvent(this, this, delay);
        Log.info(this, "Schedule end driving END");

    }

    private void endVehicleFromLastTick() {
        Log.info(this, "Ending vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: ");

        VehicleEndData vehicleEndData = (VehicleEndData) vehicleEventData;
        endDriving(vehicleEndData.vehicleTripData, vehicleEndData.lane);
        vehicleEventData = null;
    }

    protected void checkDrivingQue(Lane lane) {
        if (!lane.drivingQueue.isEmpty()) {
            long currentTime = congestionModel.timeProvider.getCurrentSimTime();
            if (currentTime >= closestCarArrivalTime) {
                closestCarArrivalTime = Long.MAX_VALUE;
            }
            Log.info(this, "checking driving queue of " + lane + " at " + currentTime);
            long firstTransferToWaitingQueueTime = lane.drivingQueue.peek().getMinPollTime();
            if (firstTransferToWaitingQueueTime < closestCarArrivalTime) {
                closestCarArrivalTime = firstTransferToWaitingQueueTime;
                congestionModel.makeTickEvent(this, this,
                        firstTransferToWaitingQueueTime - congestionModel.timeProvider.getCurrentSimTime());
            }
        }
    }
}
