package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 * Congested trip (of specific vehicle)
 * @author Zdenek Bousa
 */
public interface CongestedTripData {
    /**
     * To obtain length of vehicle
     * @return PhysicalVehicle
     */
    PhysicalVehicle getVehicle();

    /**
     * Trip
     * @return list of nodes
     */
    Trip<SimulationNode> getTrip();

    /**
     * Goal
     * @return node
     */
    SimulationNode getGoal();

    /**
     * Trip status
     * @return true if agent has met its goal
     */
    boolean isTripFinished();

    /**
     * Set trip status
     * @param tripFinished
     */
    void setTripFinished(boolean tripFinished);
}
