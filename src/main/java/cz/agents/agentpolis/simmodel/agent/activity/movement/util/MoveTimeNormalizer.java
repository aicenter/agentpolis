package cz.agents.agentpolis.simmodel.agent.activity.movement.util;


/**
 * Move time has to be greater or equals one simulation step = 1 millis
 * 
 * 
 * @author Zbynek Moler
 *
 */
public final class MoveTimeNormalizer {

	private static final long ONE_SIMULATION_STEP_IN_MILLIS = 1;
	
	private MoveTimeNormalizer(){}
	
	/**
	 * Return normalize move time, if time is less then shortest simulation step 1 ms, then return 1.
	 * In other cases return movetime.
	 * 
	 * @param moveTime
	 * @return
	 */
	public static long normalizeMoveTimeForQueue(long moveTime){
		if(moveTime < ONE_SIMULATION_STEP_IN_MILLIS){
			moveTime = ONE_SIMULATION_STEP_IN_MILLIS;
		}
		return moveTime;
	}
	
}
