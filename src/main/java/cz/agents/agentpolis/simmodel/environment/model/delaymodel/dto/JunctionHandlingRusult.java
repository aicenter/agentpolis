package cz.agents.agentpolis.simmodel.environment.model.delaymodel.dto;

import java.util.Set;

import cz.agents.agentpolis.simmodel.environment.model.delaymodel.key.FromToPositionKey;

/**
 * It is data wrapper for information about resolved specific crossroad.
 * (Messaging Design Pattern) 
 *  
 * 
 * @author Zbynek Moler
 *
 */
public class JunctionHandlingRusult {

	public final Set<FromToPositionKey> releasedDirectionToCrossroad;
	public final Set<FromToPositionKey> waitingForReleasingDirection;
	
	public JunctionHandlingRusult(Set<FromToPositionKey> releasedDirectionToCrossroad,
			Set<FromToPositionKey> waitingForReleasingDirection) {
		super();
		this.releasedDirectionToCrossroad = releasedDirectionToCrossroad;
		this.waitingForReleasingDirection = waitingForReleasingDirection;
	}
	
	
	
	
		 
	
}
