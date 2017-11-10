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

import java.util.logging.Level;

/**
 * @author fido
 */
public class Connection extends EventHandlerAdapter {

    protected final CongestionModel congestionModel;

    protected final SimulationProvider simulationProvider;

    protected final SimulationNode node;

    private CongestionLane inCongestionLane;

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

    protected void transferVehicle(VehicleTripData vehicleData, CongestionLane currentCongestionLane, CongestionLane nextCongestionLane) {
        currentCongestionLane.removeFromQueue(vehicleData);

        nextCongestionLane.addToQue(vehicleData);

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
        if (!inCongestionLane.hasWaitingVehicles()) {
            checkDrivingQue(inCongestionLane);
            return false;
        }

        // first vehicle
        VehicleTripData vehicleTripData = inCongestionLane.getFirstWaitingVehicle();

        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            scheduleEndDriving(vehicleTripData, inCongestionLane);
            return false;
        }

        CongestionLane nextCongestionLane = getNextLane(inCongestionLane, vehicleTripData);

        // succesfull transfer
        if (nextCongestionLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            scheduleVehicleTransfer(vehicleTripData, inCongestionLane, nextCongestionLane);
            return false;
        }
        // next queue is full
        else {
            Log.log(Connection.class, Level.FINE, "Connection {0}: No space in queue to {1}!", node.id,
                    nextCongestionLane.parentLink.toNode.id);
            nextCongestionLane.setWakeConnectionAfterTransfer(true);
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


    protected CongestionLane getNextLane(CongestionLane congestionLane, VehicleTripData vehicleTripData) {
        Link nextLink = getNextLink(congestionLane);

        Trip<SimulationNode> trip = vehicleTripData.getTrip();

        if (trip.isEmpty()) {
            return nextLink.getCongestionLaneForTripEnd();
        } else {
            SimulationNode NextNextLocation = trip.getFirstLocation();
            return nextLink.getBestLaneByNextNode(NextNextLocation);
        }
    }

    protected Link getNextLink(CongestionLane inputCongestionLane) {
        return outLink;
    }

    protected void endDriving(VehicleTripData vehicleTripData, CongestionLane congestionLane) {
        congestionLane.removeFromQueue(vehicleTripData);

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

    void setOutLink(Link outLink, CongestionLane inCongestionLane) {
        this.outLink = outLink;
        this.inCongestionLane = inCongestionLane;
    }

    private void transferVehicleFromLastTick() {
        VehicleTransferData vehicleTransferData = (VehicleTransferData) vehicleEventData;
        transferVehicle(vehicleTransferData.vehicleTripData, vehicleTransferData.from, vehicleTransferData.to);
        Log.info(this, "Transfering vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: " + vehicleTransferData.transferFinishTime);
        vehicleEventData = null;
    }

    void scheduleVehicleTransfer(VehicleTripData vehicleTripData, CongestionLane from, CongestionLane to) {
        Log.info(this, "Scheduling vehicle transfer START {0}",congestionModel.timeProvider.getCurrentSimTime() );
        long delay = computeTransferDelay(vehicleTripData, to);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleTransferData(from, to, vehicleTripData, transferFinishTime);


        /* next que capacity reservation */
        to.prepareAddingToqueue(vehicleTripData);

        if (vehicleTripData.getTrip().isEmpty()) {
            vehicleTripData.setTripFinished(true);
        }

        congestionModel.makeScheduledEvent(this, this, delay);
        Log.info(this, "Scheduling vehicle transfer END");
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

    void scheduleEndDriving(VehicleTripData vehicleTripData, CongestionLane congestionLane) {
        Log.info(this, "Schedule end driving START");

//        long delay = CongestionModel.computeFreeFlowTransferDelay(vehicleTripData.getVehicle());
        long delay = congestionModel.computeArrivalDelay(vehicleTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleEndData(congestionLane, vehicleTripData, transferFinishTime);

//        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, delay);
        congestionModel.makeScheduledEvent(this, this, delay);
        Log.info(this, "Schedule end driving END");

    }

    private void endVehicleFromLastTick() {
        Log.info(this, "Ending vehicle from last tick: currentTime:" + congestionModel.timeProvider.getCurrentSimTime() + " tr_finish_time: ");

        VehicleEndData vehicleEndData = (VehicleEndData) vehicleEventData;
        endDriving(vehicleEndData.vehicleTripData, vehicleEndData.congestionLane);
        vehicleEventData = null;
    }

    protected void checkDrivingQue(CongestionLane congestionLane) {
        if (!congestionLane.drivingQueue.isEmpty()) {
            long currentTime = congestionModel.timeProvider.getCurrentSimTime();
            if (currentTime >= closestCarArrivalTime) {
                closestCarArrivalTime = Long.MAX_VALUE;
            }
            Log.info(this, "checking driving queue of " + congestionLane + " at " + currentTime);
            long firstTransferToWaitingQueueTime = congestionLane.drivingQueue.peek().getMinPollTime();
            if (firstTransferToWaitingQueueTime < closestCarArrivalTime) {
                closestCarArrivalTime = firstTransferToWaitingQueueTime;
                congestionModel.makeTickEvent(this, this,firstTransferToWaitingQueueTime - congestionModel.timeProvider.getCurrentSimTime());
            }
        }
    }
}
