package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The wrapper for the sequence of trips
 *
 * @author Zbynek Moler
 */
public class Trips implements CloneTrip, Serializable, Iterable<GraphTrip<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = 9057034765971304800L;
    
    private final LinkedList<GraphTrip<?>> trips = new LinkedList<GraphTrip<?>>();

    public void addTrip(GraphTrip<?> trip) {
        trips.add(trip);
    }

    public void addBeforeCurrentTrips(GraphTrip<?> trip) {
        trips.addFirst(trip);
    }

    public void addEndCurrentTrips(GraphTrip<?> trip) {
        trips.addLast(trip);
    }

    public GraphTrip<?> getAndRemoveFirstTrip() {
        return trips.poll();
    }

    public boolean hasTrip() {
        return trips.size() > 0;
    }

    public int numTrips() {
        return trips.size();
    }

    @Override
    public Trips clone() {
        Trips newCloneTrips = new Trips();
        for (GraphTrip<?> tTrip : trips) {
            newCloneTrips.addTrip(tTrip.clone());
        }
        return newCloneTrips;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        GraphTrip<?> lastTrip = null;
        for (GraphTrip<?> t : trips) {

            if (t != null) {
                builder.append(" To: ");
                builder.append(t.showLastTripItem().toString());
                builder.append(" - ");
                builder.append(t.toString());
                builder.append('\n');
                lastTrip = t;
            } else {
                builder.append(" Null trip\n");
            }
        }
        if (lastTrip != null) {
            return builder.insert(0, lastTrip.showLastTripItem().toString()).insert(0, "Final to: ").toString();
        } else {
            return builder.toString();
        }
    }

    /**
     * The iterator servers just for showing of element without removing
     */
    @Override
    public Iterator<GraphTrip<?>> iterator() {

        return new Iterator<GraphTrip<?>>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < trips.size();
            }

            @Override
            public GraphTrip<?> next() {
                checkCondition();
                return trips.get(i++);
            }

            private void checkCondition() {
                if (hasNext() == false) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

}
