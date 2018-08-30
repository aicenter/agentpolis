package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network;

public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException() {
        super("Agentdriv could not construct path from list of nodes.");
    }
}
