/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

public class VehicleTripData {
    private final PhysicalVehicle vehicle;
    private final Trip<SimulationNode> trip;
    private boolean tripFinished;
    private SimulationNode goal;

    public VehicleTripData(PhysicalVehicle vehicle, Trip<SimulationNode> trip) {
        this.vehicle = vehicle;
        this.trip = trip;
        this.tripFinished = false;
        this.goal = trip.getLocations().getLast();
    }

    public PhysicalVehicle getVehicle() {
        return vehicle;
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    public SimulationNode getGoal() {
        return goal;
    }

    public boolean isTripFinished() {
        return tripFinished;
    }

    public void setTripFinished(boolean tripFinished) {
        this.tripFinished = tripFinished;
    }


}
