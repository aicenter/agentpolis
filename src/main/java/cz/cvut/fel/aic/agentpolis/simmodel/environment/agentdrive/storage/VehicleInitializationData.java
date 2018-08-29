package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import java.util.List;

public class VehicleInitializationData {

    private String id;
    private double velocity;
    private List<Long> startingNodeId; //change to node list? (full known route?)
    private long departureTime;

    public VehicleInitializationData(String id, double velocity, List<Long> startingNodeId, long departureTime) {
        this.id = id;
        this.velocity = velocity;
        this.startingNodeId = startingNodeId;
        this.departureTime = departureTime;
    }

    public String getId() {
        return id;
    }

    public double getVelocity() {
        return velocity;
    }

    public List<Long> getStartingNodeId() {
        return startingNodeId;
    }

    public long getDepartureTime() {
        return departureTime;
    }

}
