package cz.agents.agentpolis.siminfrastructure.planner.trip;

import java.util.LinkedList;

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
	
	
	
	public Trip(LinkedList<L> locations) throws TripException {
		checkLocations(locations);
		this.locations = locations;
	}
	
	public Trip(L startLocation, L endLocation) throws TripException {
		if(startLocation == null || endLocation == null){
			throw new TripException();
		}
		
		locations = new LinkedList<>();
        locations.add(startLocation);
        locations.add(endLocation);
	}
	
	
	
	public void extendTrip(L location) throws TripException{
		if(location == null){
			throw new TripException();
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

	private void checkLocations(LinkedList<L> locations) throws TripException {
		for (L location : locations) {
			if(location == null){
				throw new TripException();
			}
		}
	}
}
