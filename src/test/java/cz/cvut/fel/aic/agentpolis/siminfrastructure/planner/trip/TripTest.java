package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;

public class TripTest {

    private GraphTrip<TripItem> trip;
    private LinkedList<TripItem> linkedList;

    @Before
    public void setUp() {
		this.linkedList = new LinkedList<TripItem>();
		linkedList.add(new TripItem(1));
		linkedList.add(new TripItem(2));
		this.trip = new VehicleTrip(linkedList, EGraphType.TRAMWAY, "vehicleId");
    }

    @Test
    public void mockCloneTest() throws CloneNotSupportedException {
        GraphTrip<?> cloneTrip = trip.clone();

        assertEquals(2, linkedList.size());

        cloneTrip.getAndRemoveFirstTripItem();
        cloneTrip.getAndRemoveFirstTripItem();

        assertEquals(2, linkedList.size());
        assertEquals(false, cloneTrip.hasNextTripItem());

    }

    @Test
    public void getGraphType() {

        assertEquals(EGraphType.TRAMWAY, trip.getGraphType());

    }

    @Test
    public void hasNextNode() {

        assertTrue(trip.hasNextTripItem());
        trip.removeFirstTripItem();
        assertTrue(trip.hasNextTripItem());
        trip.removeFirstTripItem();
        assertFalse(trip.hasNextTripItem());
    }

    @Test
    public void showCurrentNode() {

        assertEquals(new TripItem(1), trip.showCurrentTripItem());
        trip.removeFirstTripItem();
        assertEquals(new TripItem(2), trip.showCurrentTripItem());

    }

    @Test
    public void getNextNodeAndRemove() {

        assertEquals(new TripItem(1), trip.showCurrentTripItem());
        trip.removeFirstTripItem();
        assertEquals(new TripItem(2), trip.showCurrentTripItem());
        trip.removeFirstTripItem();
        assertFalse(trip.hasNextTripItem());

    }

    @Test
    public void removeCurrentNode() {
        assertEquals(new TripItem(1), trip.showCurrentTripItem());
        trip.removeFirstTripItem();
        assertEquals(new TripItem(2), trip.showCurrentTripItem());
        trip.removeFirstTripItem();
        assertFalse(trip.hasNextTripItem());
    }

    @Test
    public void isEqualWithFirstNodeInTrip() {

        assertTrue(trip.isEqualWithFirstTripItem(new TripItem(1)));
        assertFalse(trip.isEqualWithFirstTripItem(new TripItem(2)));
        trip.removeFirstTripItem();
        assertTrue(trip.isEqualWithFirstTripItem(new TripItem(2)));

    }

    @Test
    public void addPositionBeforeCurrentTrip() {
        trip.addTripItemBeforeCurrentFirst(new TripItem(3));
        assertEquals(new TripItem(3), trip.showCurrentTripItem());

    }

    @Test
    public void numOfCurrentTripItems() {
        assertEquals(2, trip.numOfCurrentTripItems());
    }

}
