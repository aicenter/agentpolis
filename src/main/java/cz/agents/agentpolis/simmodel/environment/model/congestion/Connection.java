/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.PeriodicTicker;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.SimulationProvider;

/**
 *
 * @author fido
 */
public class Connection extends PeriodicTicker{
    
    private Lane inLane;
    
    private Link outLink;

    public Connection(SimulationProvider simulationProvider, Config config) {
        super(simulationProvider, config.connectionTickLength, ConnectionEvent.TICK);
    }

    @Override
    protected void handleTick() {
        while(!inLane.hasWaitingVehicles()){
            if(!tryTransferVehicle(inLane)){
                break;
            }
        }
    }
    

    protected void transferVehicle(PhysicalVehicle vehicle, Lane chosenLane, Lane nextLane) {
        nextLane.addToQue(vehicle);
        chosenLane.removeFromTop();
    }

    protected boolean tryTransferVehicle(Lane lane) {
        VehicleTripData vehicleTripData = lane.getFirstWaitingVehicle();
        Lane nextLane = getNextLane(vehicleTripData.getTrip());
        if(nextLane.queueHasSpaceForVehicle(vehicleTripData)){
            transferVehicle(vehicleTripData, lane, nextLane);
            return true;
        }
        return false;
    }

    private Lane getNextLane(Trip<SimulationNode> trip) {
        Link nextLink = getNextLink();
        NextNextLocation = 
    }

    protected Link getNextLink() {
        return outLink;
    }
    
    
}
