package cz.agents.agentpolis.siminfrastructure.planner.trip;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * The wrapper for the sequence of trips
 *
 * @author Zbynek Moler
 */
public class Trips implements CloneTrip, Serializable, Iterable<Trip<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = 9057034765971304800L;
    
    private final LinkedList<Trip<?>> trips = new LinkedList<Trip<?>>();

    public void addTrip(Trip<?> trip) {
        trips.add(trip);
    }

    public void addBeforeCurrentTrips(Trip<?> trip) {
        trips.addFirst(trip);
    }

    public void addEndCurrentTrips(Trip<?> trip) {
        trips.addLast(trip);
    }

    public Trip<?> getAndRemoveFirstTrip() {
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
        for (Trip<?> tTrip : trips) {
            newCloneTrips.addTrip(tTrip.clone());
        }
        return newCloneTrips;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        Trip<?> lastTrip = null;
        for (Trip<?> t : trips) {

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
    public Iterator<Trip<?>> iterator() {

        return new Iterator<Trip<?>>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < trips.size();
            }

            @Override
            public Trip<?> next() {
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
