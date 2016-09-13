package cz.agents.agentpolis.siminfrastructure.planner.trip;

/**
 * 
 * The implementation of {@code DepartureTripItem} which is under waiting
 * constrains for a departure. Providing information about the departure time
 * 
 * @author Zbynek Moler
 * 
 */
public class WaitingDepartureTripItem extends DepartureTripItem {

    public final long departureTime;
    public final boolean overMidnight;

    public WaitingDepartureTripItem(int tripPositionByNodeId, long departureTime,
                                    boolean overMidnight) {
        super(tripPositionByNodeId);
        this.departureTime = departureTime;
        this.overMidnight = overMidnight;
    }

    @Override
    public void visitDepartureTripItem(DepartureTripItemVisitor departureTripItemVisitor) {
        departureTripItemVisitor.visitDepartureTripItem(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (departureTime ^ (departureTime >>> 32));
        result = prime * result + (overMidnight ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        WaitingDepartureTripItem other = (WaitingDepartureTripItem) obj;
        if (departureTime != other.departureTime) {
            return false;
        }
        return overMidnight == other.overMidnight;
    }

}
