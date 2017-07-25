package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

/**
 * 
 * The abstraction of {@code DepartureTripItem} which is not under waiting
 * constrains for a departure.
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class DepartureTripItem extends TripItem {

    public DepartureTripItem(int tripPositionByNodeId) {
        super(tripPositionByNodeId);
        // TODO Auto-generated constructor stub
    }

    public abstract void visitDepartureTripItem(DepartureTripItemVisitor departureTripItemVisitor);

}
