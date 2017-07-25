package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

/**
 *
 * @author F.I.D.O.
 */
public class TripException extends Exception{
	
	private static final String NULL_LOCATION = "Trip location cannot be null";

	@Override
	public String getMessage() {
		return NULL_LOCATION;
	}
	
}
