package cz.agents.agentpolis.siminfrastructure.planner.trip;

/**
 * 
 * The visitor for the determination of the type of {@code DepartureTripItem}
 * 
 * @author Zbynek Moler
 * 
 */
public interface DepartureTripItemVisitor {

    public void visitDepartureTripItem(NotWaitingDepartureTripItem notWaitingDepartureTripItem);

    public void visitDepartureTripItem(WaitingDepartureTripItem waitingDepartureTripItem);

}
