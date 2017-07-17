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
import cz.agents.agentpolis.simmodel.agent.Driver;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;

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


        serveLanes();

        timeOfLastWakeUp = congestionModel.timeProvider.getCurrentSimTime();
    }

    protected void transferVehicle(VehicleTripData vehicleData, Lane currentLane, Lane nextLane) {
        currentLane.removeFromTop(vehicleData);

//        long delay = nextLane.computeMinExitTime(vehicleData.getVehicle());
//
//        // for visio
//        Driver driver =  vehicleData.getVehicle().getDriver();
//        driver.setTargetNode(nextLane.link.toNode);
//        vehicleData.getVehicle().setPosition(nextLane.link.fromNode);
//        driver.setDelayData(new DelayData(delay, congestionModel.getTimeProvider().getCurrentSimTime()));

        long delay = computeDelayAndSetVehicleData(vehicleData, nextLane.link);

        nextLane.addToQue(vehicleData, delay);

        if (!vehicleData.getTrip().isEmpty()) {
            vehicleData.getTrip().removeFirstLocation();
        }

        // wake up next connection
        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLane.link.toNode);
        wakeUpConnection(nextConnection, delay);
    }


    protected boolean tryTransferVehicle(Lane lane) {
        if (!lane.hasWaitingVehicles()) return false;
        VehicleTripData vehicleTripData = lane.getFirstWaitingVehicle();
        if (vehicleTripData.isTripFinished()) {
            endDriving(vehicleTripData, lane);
            return true;
        }

        Lane nextLane = getNextLane(lane, vehicleTripData);

        if (nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())) {
            transferVehicle(vehicleTripData, lane, nextLane);
            return true;
        }

        return false;
    }

    void startDriving(VehicleTripData vehicleData) {
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();

//        // for visio
//        Driver driver = vehicleData.getVehicle().getDriver();
//        driver.setTargetNode(nextLocation);
//        driver.setDelayData(new DelayData(minExitTime, timeProvider.getCurrentSimTime()));

        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLocation);
        Link nextLink = getNextLink(nextConnection);

        long delay = computeDelayAndSetVehicleData(vehicleData, nextLink);
        System.out.println(delay);
        if (delay <= 0) {
            Log.error(this,"Delay "+delay+" <= 0 !!!, vehicleData:" + vehicleData + ", nextLink: "+nextLink);
        }
        nextLink.startDriving(vehicleData, delay);
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

    protected Link getNextLink(Connection nextConnection) {
        return outLink;
    }

    private long computeDelayAndSetVehicleData(VehicleTripData vehicleData, Link nextLink) {
        long delay = nextLink.computeDelay(vehicleData.getVehicle());


        // for visio
        Driver driver = vehicleData.getVehicle().getDriver();
        driver.setTargetNode(nextLink.toNode);
        vehicleData.getVehicle().setPosition(nextLink.fromNode);
        driver.setDelayData(new DelayData(delay, congestionModel.getTimeProvider().getCurrentSimTime()));

        return delay;
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
