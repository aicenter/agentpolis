package cz.cvut.fel.aic.agentpolis.simmodel.environment.sensor;

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
