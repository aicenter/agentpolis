/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.Message;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;
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

    private long timeOfLastWakeUp;

    protected boolean awake = false;
    
    protected int metersTransferedThisBatch;
    
    protected int metersTransferedThisTick;


    public Connection(SimulationProvider simulationProvider, Config config, CongestionModel congestionModel,
                      SimulationNode node) {
        this.congestionModel = congestionModel;
        this.simulationProvider = simulationProvider;
        this.node = node;
        timeOfLastWakeUp = 0;
    }


    @Override
    public void handleEvent(Event event) {

        // check wake up not quicker then crossroad frequency.
        long remainingTimeToSleep = timeOfLastWakeUp + congestionModel.config.congestionModel.connectionTickLength
                - congestionModel.timeProvider.getCurrentSimTime();

        if (remainingTimeToSleep > 0) {
            if (!awake) {
                simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null,
                        remainingTimeToSleep);
                awake = true;
            }
            return;
        }

        metersTransferedThisBatch = 0;
        metersTransferedThisTick = 0;
        
        serveLanes();

        timeOfLastWakeUp = congestionModel.timeProvider.getCurrentSimTime();
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


    protected boolean tryTransferVehicle(Lane lane) {
        // no vehicles in queue
        if (!lane.hasWaitingVehicles()){
            return false;
        }
        
        // first vehicle
        VehicleTripData vehicleTripData = lane.getFirstWaitingVehicle();
        
        // vehicle ends on this node
        if (vehicleTripData.isTripFinished()) {
            endDriving(vehicleTripData, lane);
            return true;
        }

        Lane nextLane = getNextLane(lane, vehicleTripData);

        // succesfull transfer
        if (nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            transferVehicle(vehicleTripData, lane, nextLane);
            double vehicleLength = vehicleTripData.getVehicle().getLength();
            metersTransferedThisBatch += vehicleLength;
            metersTransferedThisTick += vehicleLength;
            return true;
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

    private Lane getNextLane(Lane lane, VehicleTripData vehicleTripData) {
        Link nextLink = getNextLink(lane);

        Trip<SimulationNode> trip = vehicleTripData.getTrip();

        if (trip.isEmpty()) {
            vehicleTripData.setTripFinished(true);
            return nextLink.getLaneForTripEnd();
        } else {
            SimulationNode NextNextLocation = trip.getFirstLocation();
            return nextLink.getLaneByNextNode(NextNextLocation);
        }
    }

    protected Link getNextLink(Lane inputLane) {
        return outLink;
    }

    private void endDriving(VehicleTripData vehicleTripData, Lane lane) {
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
        while (inLane.hasWaitingVehicles()) {
            if (!tryTransferVehicle(inLane)) {
                // wake up after some time
                simulationProvider.getSimulation().addEvent(ConnectionEvent.TICK, this, null, null,
                        congestionModel.config.congestionModel.connectionTickLength);
                break;
            }
        }
    }

    void setOutLink(Link outLink, Lane inLane) {
        this.outLink = outLink;
        this.inLane = inLane;
    }

}
