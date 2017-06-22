package cz.agents.agentpolis.siminfrastructure.planner.trip;

import cz.agents.agentpolis.siminfrastructure.Log;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.logging.Level;

/**
 * @param <L> locationType
 * @author F.I.D.O.
 */
public class Trip<L> {
    protected final LinkedList<L> locations;


    public LinkedList<L> getLocations() {
        return locations;
    }

    public Trip(L... locations) {
        this(new LinkedList<>(Arrays.asList(locations)));
    }

    public Trip(LinkedList<L> locations) {
        try {
            checkLocations(locations);
        } catch (TripException ex) {
            Log.log(this, Level.SEVERE, ex.getMessage());
        }
        this.locations = locations;
    }

    public Trip(L startLocation, L endLocation) {
        if (startLocation == null || endLocation == null) {
            try {
                throw new TripException();
            } catch (TripException ex) {
                Log.log(this, Level.SEVERE, ex.getMessage());
            }
        }

        locations = new LinkedList<>();
        locations.add(startLocation);
        locations.add(endLocation);
    }


    public void extendTrip(L location) {
        if (location == null) {
            try {
                throw new TripException();
            } catch (TripException ex) {
                Log.log(this, Level.SEVERE, ex.getMessage());
            }
        }
        locations.addLast(location);
    }

    public String locationsToString() {
        String str = "";

        for (L location : locations) {
            str += location.toString() + System.getProperty("line.separator");
        }

        return str;
    }

    public L getAndRemoveFirstLocation() {
        return locations.poll();
    }
    
    public L getFirstLocation() {
        return locations.peek();
    }
    
    public L removeFirstLocation() {
        return locations.removeFirst();
    }
    

    public boolean isEmpty() {
        return locations.isEmpty();
    }

    private void checkLocations(LinkedList<L> locations) throws TripException {
        for (L location : locations) {
            if (location == null) {
                throw new TripException();
            }
        }
    }
}
