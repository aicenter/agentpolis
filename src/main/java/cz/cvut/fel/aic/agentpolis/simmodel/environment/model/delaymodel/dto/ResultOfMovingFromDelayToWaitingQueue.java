package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel.dto;

/**
 * Result of moving between in and out queue
 * 
 * @author Zbynek Moler
 *
 */
public class ResultOfMovingFromDelayToWaitingQueue {

	public final boolean wasMoved;
	public final long queueDelay;
	
	public ResultOfMovingFromDelayToWaitingQueue(boolean wasMoved) {
		this.wasMoved = wasMoved;
		this.queueDelay = -1; // unreachable value - it will be lead to exception 
	}
	
	public ResultOfMovingFromDelayToWaitingQueue(boolean wasMoved, long queueDelay) {	
		this.wasMoved = wasMoved;
		this.queueDelay = queueDelay;
	}
	
	
	
}
