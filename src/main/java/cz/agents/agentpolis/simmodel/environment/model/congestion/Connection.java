/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.PeriodicTicker;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.Message;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;

/**
 *
 * @author fido
 */
public class Connection extends PeriodicTicker{
    
    private final CongestionModel congestionModel;
    
    private Lane inLane;
    
    private Link outLink;

    public Connection(SimulationProvider simulationProvider, Config config, CongestionModel congestionModel) {
        super(simulationProvider, config.connectionTickLength, ConnectionEvent.TICK);
        this.congestionModel = congestionModel;
    }

    @Override
    protected void handleTick() {
        while(!inLane.hasWaitingVehicles()){
            if(!tryTransferVehicle(inLane)){
                break;
            }
        }
    }
    

    protected void transferVehicle(VehicleTripData vehicleData, Lane chosenLane, Lane nextLane) {
        chosenLane.removeFromTop();
        nextLane.addToQue(vehicleData);
        
    }

    protected boolean tryTransferVehicle(Lane lane) {
        VehicleTripData vehicleTripData = lane.getFirstWaitingVehicle();
        Lane nextLane = getNextLane(vehicleTripData.getTrip());
        if(vehicleTripData.getTrip().isEmpty()){
            endDriving(vehicleTripData);
        }
        if(nextLane.queueHasSpaceForVehicle(vehicleTripData.getVehicle())){
            transferVehicle(vehicleTripData, lane, nextLane);
            return true;
        }
        return false;
    }
	
	void startDriving(VehicleTripData vehicleData){
        Trip<SimulationNode> trip = vehicleData.getTrip();
        SimulationNode nextLocation = trip.getAndRemoveFirstLocation();
        Connection nextConnection = congestionModel.connectionsMappedByNodes.get(nextLocation);
        getNextLink(nextConnection).startDriving(vehicleData);
    }

    private Lane getNextLane(Trip<SimulationNode> trip) {
        Link nextLink = getNextLink(inLane);
        SimulationNode NextNextLocation = trip.getAndRemoveFirstLocation();
        return nextLink.getLaneByNextNode(NextNextLocation);
    }

    protected Link getNextLink(Lane inputLane) {
        return outLink;
    }

    private void endDriving(VehicleTripData vehicleTripData) {
        
        // todo - remove typing
        ((Agent) vehicleTripData.getVehicle().getDriver()).processMessage(new Message(
                CongestionMessage.DRIVING_FINISHED, null));
    }

    protected Link getNextLink(Connection nextConnection) {
        return outLink;
    }
    
    
}
