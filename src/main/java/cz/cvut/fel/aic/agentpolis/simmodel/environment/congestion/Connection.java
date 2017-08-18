/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
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
    
    private VehicleTransferData vehicleTransferData;
    


    public Connection(SimulationProvider simulationProvider, Config config, CongestionModel congestionModel,
                      SimulationNode node) {
        this.congestionModel = congestionModel;
        this.simulationProvider = simulationProvider;
        this.node = node;
        isTicking = false;
    }


    @Override
    public void handleEvent(Event event) {
        
        /* if event comes while connection is woken up */
        if(vehicleTransferData != null 
                && vehicleTransferData.transferFinishTime > congestionModel.timeProvider.getCurrentSimTime()){
            return;
        }
        
        /* if the vehicle should be transfere by this event */
        if(vehicleTransferData != null){
            transferVehicleFromLastTick();
        }
        
        serveLanes();
    }

    protected void transferVehicle(VehicleTripData vehicleData, Lane currentLane, Lane nextLane) {
        currentLane.removeFromTop(vehicleData);
        
        long delay = congestionModel.computeDelayAndSetVehicleData(vehicleData, nextLane);
        
        nextLane.addToQue(vehicleData, delay);

        if (!vehicleData.getTrip().isEmpty()) {
            vehicleData.getTrip().removeFirstLocation();
        }

        // wake up next connection
        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLane.link.toNode);
        wakeUpConnection(nextConnection, delay);
        
        // wake previous connection if current lane was full
        if(currentLane.wakeConnectionAfterTransfer()){
            Connection previousConnection = currentLane.link.fromConnection;
            wakeUpConnection(previousConnection, 0);
            currentLane.setWakeConnectionAfterTransfer(false);
        }
    }

    /**
     * Tries to transffer one vehicle. The vehicle will be transfered in next tick.
     * @return true if next vehicle can be tried, false otherwise.
     */
    private boolean tryTransferVehicle() {
        
        /* no one is waiting */
        if(!inLane.hasWaitingVehicles()){
            return false;
        }
        
        // first vehicle
        VehicleTripData vehicleTripData = inLane.getFirstWaitingVehicle();
        
        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            endDriving(vehicleTripData, inLane);
            return true;
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

        long delay = nextLink.startDriving(vehicleData);
        wakeUpConnection(nextConnection, delay);
    }

    private void wakeUpConnection(Connection nextConnection, long delay) {
        // wake up next connection
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, nextConnection, null, null, delay + 80);
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
        lane.removeFromTop(vehicleTripData);

        vehicleTripData.getVehicle().setPosition(node);

        // todo - remove typing
        ((Agent) vehicleTripData.getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));
    }

    public Link getNextLink(Connection nextConnection) {
        return outLink;
    }
    
    

    protected void serveLanes() {
        while(tryTransferVehicle()){        
        }
    }

    void setOutLink(Link outLink, Lane inLane) {
        this.outLink = outLink;
        this.inLane = inLane;
    }

    private void transferVehicleFromLastTick() {
        transferVehicle(vehicleTransferData.vehicleTripData, vehicleTransferData.from, vehicleTransferData.to);
        vehicleTransferData = null;
    }

    protected void scheduleVehicleTransfer(VehicleTripData vehicleTripData, Lane from, Lane to) {
        long delay = computeTransferDelay(vehicleTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleTransferData = new VehicleTransferData(from, to, vehicleTripData, transferFinishTime);
        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, delay);
    }

    protected long computeTransferDelay(VehicleTripData vehicleTripData) {
        return Math.round(vehicleTripData.getVehicle().getLength() * 1E3 / vehicleTripData.getVehicle().getVelocity());
    }

    
    
    
    private class VehicleTransferData{
        private final Lane from;
        
        private final Lane to;
        
        private final VehicleTripData vehicleTripData;
        
        private final long transferFinishTime;

        public VehicleTransferData(Lane from, Lane to, VehicleTripData vehicleTripData, long transferFinishTime) {
            this.from = from;
            this.to = to;
            this.vehicleTripData = vehicleTripData;
            this.transferFinishTime = transferFinishTime;
        }
    }
}
