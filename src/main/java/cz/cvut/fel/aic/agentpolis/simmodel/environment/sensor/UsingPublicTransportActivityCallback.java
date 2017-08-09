package cz.cvut.fel.aic.agentpolis.simmodel.environment.sensor;

/**
 * Declaration of callbacks method for using public transport activity
 * 
 * @author Zbynek Moler
 *
 */
public interface UsingPublicTransportActivityCallback {

	/**
	 * It is invoked, after ending of using public transport (executed all trips )
	 */
	public void finishedUsingPublicTransport();
}
