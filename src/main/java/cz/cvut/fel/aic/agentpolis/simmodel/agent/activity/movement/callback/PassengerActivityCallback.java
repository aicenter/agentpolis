package cz.cvut.fel.aic.agentpolis.simmodel.agent.activity.movement.callback;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;

/**
 * Declaration of callback methods for passenger activity
 * 
 * @author Zbynek Moler
 * 
 * @param <TTrip>
 */
public interface PassengerActivityCallback<TTrip extends GraphTrip<?>> {

    /**
     * Callback method - it is invoked, if passenger finished traveling by a
     * vehicle and passenger trip (plan) was completed
     */
    public void doneFullTrip();

    /**
     * Callback method - it is invoked, if passenger finished traveling by some
     * vehicle and passenger trip (plan) was not completed, but part was
     * 
     */
    public void donePartTrip(TTrip partNotDoneTrip);

    /**
     * Callback method - it is invoked, if passenger finished traveling by some
     * vehicle, passenger trip (plan) was not completed - fail (e.g. vehicle was
     * full occupied)
     * 
     */
    public void tripFail(TTrip failedTrip);
    
    public void tripStarted();

}
