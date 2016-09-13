package cz.agents.agentpolis.siminfrastructure.planner.trip;

/**
 * 
 * The implementation of {@code DepartureTripItem} which is not under waiting
 * constrains for a departure.
 * 
 * @author Zbynek Moler
 * 
 */
public class NotWaitingDepartureTripItem extends DepartureTripItem {

    public NotWaitingDepartureTripItem(int tripPositionByNodeId) {
        super(tripPositionByNodeId);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void visitDepartureTripItem(DepartureTripItemVisitor departureTripItemVisitor) {
        departureTripItemVisitor.visitDepartureTripItem(this);

    }

}
