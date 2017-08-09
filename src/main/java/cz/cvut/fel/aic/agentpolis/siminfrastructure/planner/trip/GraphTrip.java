package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;


import java.io.Serializable;
import java.util.Iterator;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.LinkedList;

/**
 * Basic locations on specific graph and path across nodes.
 * 
 * @author Zbynek Moler
 * @param <TTripItem> Location type
 * 
 */
public abstract class GraphTrip<TTripItem extends TripItem> extends Trip<TTripItem> implements Serializable {

    private static final long serialVersionUID = 8223815753061840075L;

    protected final GraphType graphType;

    public GraphTrip(LinkedList<TTripItem> locations, GraphType graphType){
        super(checkNotNull(locations));
        this.graphType = graphType;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public boolean hasNextTripItem() {
        return locations.size() > 0;
    }

    public TTripItem showCurrentTripItem() {
        return locations.peek();
    }

    public TTripItem showNextTripItem() {
        return locations.iterator().next();
    }

    public TTripItem showLastTripItem() {
        return locations.peekLast();
    }

    public TTripItem getAndRemoveFirstTripItem() {
        return locations.poll();
    }

    public void removeFirstTripItem() {
        locations.poll();
    }

    public boolean isEqualWithFirstTripItem(TTripItem tripItem) {
        return locations.peekFirst().equals(tripItem);
    }

    // public boolean isEqualWithLastNodeInTrip(long nodeId) {
    // return locations.peekLast() == nodeId;
    // }

    public void addTripItemBeforeCurrentFirst(TTripItem tripItem) {
        locations.addFirst(tripItem);
    }

    public int numOfCurrentTripItems() {
        return locations.size();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder(graphType.toString());
        stringBuilder.append('(');
        if (!locations.isEmpty()) {
            Iterator<TTripItem> iterator = locations.iterator();
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

    public abstract GraphTrip<TTripItem> clone();

}
