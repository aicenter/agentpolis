package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback;

/**
 * Declaration of callback methods for movement activity
 * 
 * @author Zbynek Moler
 * 
 */
public interface MovementActivityCallback {

    /**
     * It is invoked, when somebody or something achieved some goal (usually
     * destination)
     */
    public void finishedMovement();

}
