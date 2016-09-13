package cz.agents.agentpolis.siminfrastructure.planner.trip;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;

/**
 * Basic trip on specific graph and path across nodes.
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class Trip<TTripItem extends TripItem> implements Serializable {

    private static final long serialVersionUID = 8223815753061840075L;

    protected final LinkedList<TTripItem> trip;
    protected final GraphType graphType;

    public Trip(LinkedList<TTripItem> trip, GraphType graphType) {
        super();
        this.trip = checkNotNull(trip);
        this.graphType = graphType;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public boolean hasNextTripItem() {
        return trip.size() > 0;
    }

    public TTripItem showCurrentTripItem() {
        return trip.peek();
    }

    public TTripItem showNextTripItem() {
        return trip.iterator().next();
    }

    public TTripItem showLastTripItem() {
        return trip.peekLast();
    }

    public TTripItem getAndRemoveFirstTripItem() {
        return trip.poll();
    }

    public void removeFirstTripItem() {
        trip.poll();
    }

    public boolean isEqualWithFirstTripItem(TTripItem tripItem) {
        return trip.peekFirst().equals(tripItem);
    }

    // public boolean isEqualWithLastNodeInTrip(long nodeId) {
    // return trip.peekLast() == nodeId;
    // }

    public void addTripItemBeforeCurrentFirst(TTripItem tripItem) {
        trip.addFirst(tripItem);
    }

    public int numOfCurrentTripItems() {
        return trip.size();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder(graphType.toString());
        stringBuilder.append('(');
        if (!trip.isEmpty()) {
            Iterator<TTripItem> iterator = trip.iterator();
            stringBuilder.append(iterator.next().tripPositionByNodeId);
            while (iterator.hasNext()) {
                stringBuilder.append('-');
                stringBuilder.append('>');
                stringBuilder.append(iterator.next().tripPositionByNodeId);
            }
        }

        stringBuilder.append(')');
        stringBuilder.append(' ');

        return stringBuilder.toString();
    }

    public abstract void visit(TripVisitior tripVisitior);

    public abstract Trip<TTripItem> clone();

}
