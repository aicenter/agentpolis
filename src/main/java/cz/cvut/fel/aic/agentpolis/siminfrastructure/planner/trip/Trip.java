/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

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
