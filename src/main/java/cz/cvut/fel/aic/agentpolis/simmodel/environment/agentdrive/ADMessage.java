package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Graph;

public class ADMessage {

    private final Vehicle vehicle;

    private final Trip<SimulationNode> trip;

    private final Graph<SimulationNode, SimulationEdge> graph;

    private final int tripId;

    private final AgentDriveModel agentDriveModel;

    public ADMessage(Vehicle vehicle, Trip<SimulationNode> trip, Graph<SimulationNode, SimulationEdge> graph, int tripId, AgentDriveModel agentDriveModel) {
        this.vehicle = vehicle;
        this.trip = trip;
        this.graph = graph;
        this.tripId = tripId;
        this.agentDriveModel = agentDriveModel;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Trip<SimulationNode> getTrip() {
        return trip;
    }

    public Graph<SimulationNode, SimulationEdge> getGraph() {
        return graph;
    }

    public int getTripId() {
        return tripId;
    }

    public AgentDriveModel getAgentDriveModel() {
        return agentDriveModel;
    }
}
