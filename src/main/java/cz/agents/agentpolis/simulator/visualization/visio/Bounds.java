package cz.agents.agentpolis.simulator.visualization.visio;

import cz.agents.basestructures.GPSLocation;

public class Bounds {

    private final GPSLocation minNode;
    private final GPSLocation maxNode;

    public Bounds(GPSLocation minNode, GPSLocation maxNode) {
        this.minNode = minNode;
        this.maxNode = maxNode;
    }

    public GPSLocation getMinNode() {
        return minNode;
    }

    public GPSLocation getMaxNode() {
        return maxNode;
    }

    public int getMaxLatE6() {
        return maxNode.getLatE6();
    }

    public int getMinLatE6() {
        return minNode.getLatE6();
    }

    public int getMinLonE6() {
        return minNode.getLonE6();
    }

    public int getMaxLonE6() {
        return maxNode.getLonE6();
    }
}
