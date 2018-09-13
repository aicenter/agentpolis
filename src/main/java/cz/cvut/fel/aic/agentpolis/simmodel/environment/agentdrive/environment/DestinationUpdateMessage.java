package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment;

public class DestinationUpdateMessage {
    int carId;
    long reachedAgentPolisId;
    public DestinationUpdateMessage(int i, long agentpolsId) {
        carId = i;
        reachedAgentPolisId = agentpolsId;
    }

    public int getCarId() {
        return carId;
    }

    public long getReachedAgentPolisId() {
        return reachedAgentPolisId;
    }
}
