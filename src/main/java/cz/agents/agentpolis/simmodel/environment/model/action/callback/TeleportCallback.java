package cz.agents.agentpolis.simmodel.environment.model.action.callback;

/**
 * Declaration of teleport callbacks method 
 * 
 * @author Zbynek Moler
 *
 */
public interface TeleportCallback {

	/**
	 * It is invoked, when entity was tepeported to new position
	 */
	public void entityWasTeleported();
	
}
