package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.dto;

import java.util.Set;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.key.FromToPositionKey;

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
