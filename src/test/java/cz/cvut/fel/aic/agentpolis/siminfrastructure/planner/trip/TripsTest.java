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

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trips;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.PTTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.Test;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;

public class TripsTest {

	@Test
	public void tripsTest() throws CloneNotSupportedException {
		LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
		linkedList.add(new TripItem(1));
		linkedList.add(new TripItem(2));
		GraphTrip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

		LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
		linkedList2.add(new TripItem(3));
		linkedList2.add(new TripItem(4));
		GraphTrip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId");

		Trips trips = new Trips();
		trips.addTrip(trip);
		trips.addTrip(trip2);

		Trips cloneTrips = trips.clone();
		assertEquals(2, cloneTrips.numTrips());
		assertEquals(2, trips.numTrips());

		GraphTrip<?> trip3 = cloneTrips.getAndRemoveFirstTrip();
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

		GraphTrip<?> trip3 = cloneTrips.getAndRemoveFirstTrip();
		assertEquals(1, cloneTrips.numTrips());
		assertEquals(2, trips.numTrips());

		assertEquals(new TripItem(1), trip3.getAndRemoveFirstTripItem());
		assertEquals(new TripItem(2), trip3.getAndRemoveFirstTripItem());
		assertFalse(trip3.hasNextTripItem());

		GraphTrip<?> trip4 = trips.getAndRemoveFirstTrip();
		assertEquals(new TripItem(1), trip4.getAndRemoveFirstTripItem());
		assertEquals(new TripItem(2), trip4.getAndRemoveFirstTripItem());

	}

	@Test
	public void tripsTest3() throws CloneNotSupportedException {
		LinkedList<TripItem> linkedList = new LinkedList<TripItem>();
		linkedList.add(new TripItem(1));
		linkedList.add(new TripItem(2));
		GraphTrip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

		LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
		linkedList2.add(new TripItem(3));
		linkedList2.add(new TripItem(4));
		GraphTrip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId1");

		Trips trips = new Trips();
		trips.addTrip(trip);
		trips.addTrip(trip2);

		Iterator<GraphTrip<?>> iterator = trips.iterator();
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
		GraphTrip<?> trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");

		LinkedList<TripItem> linkedList2 = new LinkedList<TripItem>();
		linkedList2.add(new TripItem(3));
		linkedList2.add(new TripItem(4));
		GraphTrip<?> trip2 = new VehicleTrip(linkedList2, EGraphType.TRAMWAY, "vehicleId1");

		Trips trips = new Trips();
		trips.addTrip(trip);
		trips.addTrip(trip2);

		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			Iterator<GraphTrip<?>> iterator = trips.iterator();
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
