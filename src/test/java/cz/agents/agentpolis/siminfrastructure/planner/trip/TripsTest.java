package cz.agents.agentpolis.siminfrastructure.planner.trip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.Test;

import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;

public class TripsTest {

    @Test
    public void tripsTest() throws CloneNotSupportedException {
        LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
        linkedList.add(new TripItem(1));
        linkedList.add(new TripItem(2));
        Trip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

        LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
        linkedList2.add(new TripItem(3));
        linkedList2.add(new TripItem(4));
        Trip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId");

        Trips trips = new Trips();
        trips.addTrip(trip);
        trips.addTrip(trip2);

        Trips cloneTrips = trips.clone();
        assertEquals(2, cloneTrips.numTrips());
        assertEquals(2, trips.numTrips());

        Trip<?> trip3 = cloneTrips.getAndRemoveFirstTrip();
        assertEquals(1, cloneTrips.numTrips());
        assertEquals(2, trips.numTrips());

        assertEquals(new TripItem(1), trip3.getAndRemoveFirstTripItem());
        assertEquals(new TripItem(2), trip3.getAndRemoveFirstTripItem());
        assertFalse(trip3.hasNextTripItem());

    }

    @Test
    public void tripsTest2() throws CloneNotSupportedException {
        LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
        linkedList.add(new TripItem(1));
        linkedList.add(new TripItem(2));
        LinkedList<String> stations = new LinkedList<String>();
        stations.add("Station 1");
        stations.add("Station 2");
        PTTrip trip = new PTTrip(linkedList, EGraphType.TRAMWAY, "linkid", stations);

        LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
        linkedList2.add(new TripItem(3));
        linkedList2.add(new TripItem(4));
        LinkedList<String> stations2 = new LinkedList<String>();
        stations2.add("Station 3");
        stations2.add("Station 4");
        PTTrip trip2 = new PTTrip(linkedList2, EGraphType.TRAMWAY, "linkid", stations2);

        Trips trips = new Trips();
        trips.addTrip(trip);
        trips.addTrip(trip2);

        Trips cloneTrips = trips.clone();
        assertEquals(2, cloneTrips.numTrips());
        assertEquals(2, trips.numTrips());

        Trip<?> trip3 = cloneTrips.getAndRemoveFirstTrip();
        assertEquals(1, cloneTrips.numTrips());
        assertEquals(2, trips.numTrips());

        assertEquals(new TripItem(1), trip3.getAndRemoveFirstTripItem());
        assertEquals(new TripItem(2), trip3.getAndRemoveFirstTripItem());
        assertFalse(trip3.hasNextTripItem());

        Trip<?> trip4 = trips.getAndRemoveFirstTrip();
        assertEquals(new TripItem(1), trip4.getAndRemoveFirstTripItem());
        assertEquals(new TripItem(2), trip4.getAndRemoveFirstTripItem());

    }

    @Test
    public void tripsTest3() throws CloneNotSupportedException {
        LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
        linkedList.add(new TripItem(1));
        linkedList.add(new TripItem(2));
        Trip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

        LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
        linkedList2.add(new TripItem(3));
        linkedList2.add(new TripItem(4));
        Trip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId1");

        Trips trips = new Trips();
        trips.addTrip(trip);
        trips.addTrip(trip2);

        Iterator<Trip<?>> iterator = trips.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().getAndRemoveFirstTripItem().tripPositionByNodeId);
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().getAndRemoveFirstTripItem().tripPositionByNodeId);
        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail(); // FAIL when no exception is thrown
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }

        assertEquals(2, trips.numTrips());
    }

    @Test
    public void tripsTestTime() throws CloneNotSupportedException {
        LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
        linkedList.add(new TripItem(1));
        linkedList.add(new TripItem(2));
        Trip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

        LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
        linkedList2.add(new TripItem(3));
        linkedList2.add(new TripItem(4));
        Trip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId1");

        Trips trips = new Trips();
        trips.addTrip(trip);
        trips.addTrip(trip2);

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Iterator<Trip<?>> iterator = trips.iterator();
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
        System.out.println(System.currentTimeMillis() - t1);

        t1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Trips clone = trips.clone();
            while (clone.hasTrip()) {
                clone.getAndRemoveFirstTrip();
            }
        }
        System.out.println(System.currentTimeMillis() - t1);
    }
}
