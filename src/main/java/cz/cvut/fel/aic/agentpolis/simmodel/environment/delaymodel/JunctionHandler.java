package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.citymodel.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.dto.JunctionHandlingRusult;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.impl.DelayModelStorage;

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
