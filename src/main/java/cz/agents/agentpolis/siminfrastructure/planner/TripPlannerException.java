package cz.agents.agentpolis.siminfrastructure.planner;

/**
 * 
 * Class represents an exception definition. This exception should be thrown, if
 * planner is not able to find a trip from start position to destination position.
 * 
 * @author Zbynek Moler
 *
 */
public class TripPlannerException extends Exception {

    private static final long serialVersionUID = 3197313361674952603L;

    private static final String EXCEPTION_MESSAGE = "Trip Planner did not find any way from %s to %s";
	
	public TripPlannerException(long startNodeById, long destinationNodeById) {
		super(String.format(EXCEPTION_MESSAGE,startNodeById,destinationNodeById));
	}
}
