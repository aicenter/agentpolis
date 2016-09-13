package cz.agents.agentpolis.simmodel.environment.model.delaymodel;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.dto.JunctionHandlingRusult;
import cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl.DelayModelStorage;

/**
 * Declaration of method, which has to be implemented by junction handler, which solving 
 * moving queue items across junction.
 * 
 * @author Zbynek Moler
 *
 */
public interface JunctionHandler {

	/**
	 * Implementation of this method has to solve moving queue items across specific junction
	 * 
	 */
	public JunctionHandlingRusult handleJunction(long junctionByNodeId, GraphType graphType,
			DelayModelStorage delayModelStorage);
	

}
