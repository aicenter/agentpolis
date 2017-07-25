package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel;

/**
 * Methods in this interface provides necessary information about one item (e.g. vehicle) in delaying segment for delay handling. 
 * 
 * @author Zbynek Moler
 *
 */
public interface DelayActor {

	/**
	 * If delay actor is chosen by {@code DelayHalder}, then it can be executed. 
	 */
	public void execute();
	
	/**
	 * Represents time, which takes go out from DelayQueue in DelayingSegment  (e.g. for vehicle - length of vehicle / vehicle velocity ). Return value is in millis
	 * @return
	 */
	public long delayTime();
	
	// TODO: return null is not good solution - thinking about NULL Object
	/**
	 * Represents future step - return id of next node or null
	 * @return
	 */
	public Long nextDestinationByNodeId();
	
	/**
	 * Return value represents space, which item will take in DelayingSegment (e.g length of vehicle)
	 * @return
	 */
	public double takenSpace();
	
	/**
	 * Provides unique id through all queue ( = should by return same id like entity - e.g. for vehicle - vehicle id, for agent - agent id)
	 * @return
	 */
	public String delayActorId();
	
}
