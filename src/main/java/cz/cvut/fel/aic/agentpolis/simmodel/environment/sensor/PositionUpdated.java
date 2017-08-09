package cz.cvut.fel.aic.agentpolis.simmodel.environment.sensor;



public interface PositionUpdated {

    public void newEntityPosition(String entityId, long nodeId);
}
