/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public class VehicleTripData {
    private final PhysicalVehicle vehicle;
    
    private final Trip<SimulationNode> trip;
    
    
    private boolean tripFinished;

    public PhysicalVehicle getVehicle() {
        return vehicle;
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    public boolean isTripFinished() {
        return tripFinished;
    }

    public void setTripFinished(boolean tripFinished) {
        this.tripFinished = tripFinished;
    }
    
    

    
    public VehicleTripData(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        this.vehicle = vehicle;
        this.trip = trip;
        tripFinished = false;
    }
    
    
}
