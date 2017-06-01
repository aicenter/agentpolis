/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public class VehicleTripData {
    private final PhysicalVehicle vehicle;
    
    private final Trip<SimulationNode> trip;

    public PhysicalVehicle getVehicle() {
        return vehicle;
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    
    public VehicleTripData(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        this.vehicle = vehicle;
        this.trip = trip;
    }
    
    
}
