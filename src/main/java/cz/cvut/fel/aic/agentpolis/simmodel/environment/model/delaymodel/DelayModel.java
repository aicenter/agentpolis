package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.delaymodel;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * Declaration of method for queue resolver, which has to be implemented
 * 
 * @author Zbynek Moler
 *
 */
public interface DelayModel {
	
	public void handleDelayActor(long fromByNodeId, long toByNodeId, GraphType graphType, DelayActor delayActor);
	
}