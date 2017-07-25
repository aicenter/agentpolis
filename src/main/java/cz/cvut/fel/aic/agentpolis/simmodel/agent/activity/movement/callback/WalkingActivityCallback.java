package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback;

/**
 * Declaration of callback methods for walking activity
 * 
 * @author Zbynek Moler
 * 
 */
public interface WalkingActivityCallback {

    /**
     * Callback method - it is invoked, if walking finished (it was executed
     * full walk plan - trip)
     */
    public void finishedWalking();

}
