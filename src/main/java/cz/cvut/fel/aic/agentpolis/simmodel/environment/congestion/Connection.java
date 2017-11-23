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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.Message;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.SimulationProvider;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEndData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleEventData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.connection.VehicleTransferData;
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
        if(vehicleEventData != null 
                && vehicleEventData.transferFinishTime > congestionModel.timeProvider.getCurrentSimTime()){
            return;
        }
        
        /* if the vehicle should be transfere by this event */
        if(vehicleEventData != null){
            if(vehicleEventData instanceof VehicleTransferData) {
                transferVehicleFromLastTick();
            }
            else{
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
     * @return true if next vehicle can be tried, false otherwise.
     */
    private boolean tryTransferVehicle() {
        
        /* no one is waiting */
        if(!inLane.hasWaitingVehicles()){
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
        while(tryTransferVehicle()){        
        }
    }

    void setOutLink(Link outLink, Lane inLane) {
        this.outLink = outLink;
        this.inLane = inLane;
    }

    private void transferVehicleFromLastTick() {
        VehicleTransferData vehicleTransferData = (VehicleTransferData) vehicleEventData;
        transferVehicle(vehicleTransferData.vehicleTripData, vehicleTransferData.from, vehicleTransferData.to);
        vehicleEventData = null;
    }

    void scheduleVehicleTransfer(VehicleTripData vehicleTripData, Lane from, Lane to) {
        long delay = computeTransferDelay(vehicleTripData);
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleTransferData(from, to, vehicleTripData, transferFinishTime);
        
        /* next que capacity reservation */
        to.prepareAddingToqueue(vehicleTripData);
        
        congestionModel.makeTickEvent(this, delay);
    }

    protected long computeTransferDelay(VehicleTripData vehicleTripData) {
        return CongestionModel.computeFreeflowTransferDelay(vehicleTripData.getVehicle());
    }
    
    void scheduleEndDriving(VehicleTripData vehicleTripData, Lane lane){
//        long delay = CongestionModel.computeFreeflowTransferDelay(vehicleTripData.getVehicle());
        long delay = 100;
        long transferFinishTime = congestionModel.timeProvider.getCurrentSimTime() + delay;
        vehicleEventData = new VehicleEndData(lane, vehicleTripData, transferFinishTime);
        
//        simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null, delay);
        congestionModel.makeTickEvent(this, delay);
    }

    private void endVehicleFromLastTick() {
        VehicleEndData vehicleEndData = (VehicleEndData) vehicleEventData;
        endDriving(vehicleEndData.vehicleTripData, vehicleEndData.lane);
        vehicleEventData = null;
    }
    
    protected void checkDrivingQue(Lane lane){
        if(!lane.drivingQueue.isEmpty()){
            long firstTransferToWaitingQueueTime = lane.drivingQueue.peek().getMinPollTime();
            congestionModel.makeTickEvent(this, 
                    firstTransferToWaitingQueueTime - congestionModel.timeProvider.getCurrentSimTime());
        }
    }
}
