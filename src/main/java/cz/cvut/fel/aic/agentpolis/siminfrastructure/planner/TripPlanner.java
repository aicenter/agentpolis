package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trips;

/**
 * 
 * 
 * Through method, which provides this interface, is possible to get trip (plan)
 * to simulation.
 * 
 * 
 * @author Zbynek Moler
 * 
 * @param <TTrip>
 */
public interface TripPlanner {

    /**
     * 
     * Returns the representation of journey described as a sequence of
     * {@code Trip}
     * 
     * @param agetnIdId
     * @param startNodeById
     * @param destinationNodeById
     * @return
     * @throws TripPlannerException
     */
    public Trips findTrip(String agetnIdId, long startNodeById, long destinationNodeById)
            throws TripPlannerException;

}
