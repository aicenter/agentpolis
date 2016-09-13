package cz.agents.agentpolis.simmodel.environment.model.sensor;

/**
 * Informs passenger before plan notify
 * 
 * @author Zbynek Moler
 *
 */
public interface PassengerBeforePlanNotifySensor {

	/**
	 * Informs passenger before plan notify
	 * 
	 * @param vehicleId
	 */
	public void beforePlanNotify(final String inspectorId,String vehicleId,long fromPositionByNodeId, long toPositionByNodeId);
	
}
