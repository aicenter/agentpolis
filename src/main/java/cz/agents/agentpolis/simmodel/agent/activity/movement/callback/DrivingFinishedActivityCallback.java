package cz.agents.agentpolis.simmodel.agent.activity.movement.callback;

/**
 * 
 * Declaration of callback methods for driving activity
 * 
 * @author Zbynek Moler
 * 
 */
public interface DrivingFinishedActivityCallback {

    /**
     * It is invoked, when agent ( vehicle includes this agent ) will arrive to
     * destination
     */
    public void finishedDriving();

}
