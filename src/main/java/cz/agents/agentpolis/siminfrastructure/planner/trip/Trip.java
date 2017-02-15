package cz.agents.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author F.I.D.O.
 * @param <L> locationType
 */
public class Trip<L> {
	protected final LinkedList<L> locations;

	
	
	public LinkedList<L> getLocations() {
		return locations;
	}
	
	
	
	public Trip(LinkedList<L> locations){
		try {
			checkLocations(locations);
		} catch (TripException ex) {
			Logger.getLogger(Trip.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.locations = locations;
	}
	
	public Trip(L startLocation, L endLocation){
		if(startLocation == null || endLocation == null){
			try {
				throw new TripException();
			} catch (TripException ex) {
				Logger.getLogger(Trip.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		locations = new LinkedList<>();
        locations.add(startLocation);
        locations.add(endLocation);
	}
	
	
	
	public void extendTrip(L location){
		if(location == null){
			try {
				throw new TripException();
			} catch (TripException ex) {
				Logger.getLogger(Trip.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		locations.addLast(location);
	}
	
	public String locationsToString(){
		String str = "";
		
		for (L location : locations) {
			str += location.toString() + System.getProperty("line.separator");
		}
		
		return str;
	}
    
    public L getAndRemoveFirstLocation(){
        return locations.poll();
    }

    public boolean isEmpty(){
        return locations.isEmpty();
    }

	private void checkLocations(LinkedList<L> locations) throws TripException {
		for (L location : locations) {
			if(location == null){
				throw new TripException();
			}
		}
	}
}
