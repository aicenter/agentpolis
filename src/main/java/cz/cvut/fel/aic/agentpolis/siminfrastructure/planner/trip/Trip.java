/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip;


import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 * @param <L> locationType
 * @author F.I.D.O.
 */
public class Trip<L> {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Trip.class);
	
	private static boolean checkLocations = false;

	public static void setCheckLocations(boolean check_locations) {
		Trip.checkLocations = check_locations;
	}
	
	
	
	
	protected final L[] locations;
	
	
	private int currentFirstLocationIndex;
	
	protected int tripId;

	
	

	public Trip(int tripId,L... locations) {
		if(checkLocations){
			try {
				checkLocations(locations);
			} catch (TripException ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}
		this.locations = locations;
		this.tripId = tripId;
		currentFirstLocationIndex = 0;
	}


//	public void extendTrip(L location) {
//		if(checkLocations){
//			if (location == null) {
//				try {
//					throw new TripException();
//				} catch (TripException ex) {
//					LOGGER.error(ex.getMessage(), ex);
//				}
//			}
//		}
//		locations = new L[];
//	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
//		if (getClass() != obj.getClass()) {
//			return false;
//		}
		final Trip<?> other = (Trip<?>) obj;
		return Objects.equals(this.locations, other.locations);
	}
	
	public L[] getLocations() {
		if(currentFirstLocationIndex == 0){
			return locations;
		}
		return Arrays.copyOfRange(locations, currentFirstLocationIndex, locations.length);
	}
	
	public L[] getAllLocations() {
		return locations;
	}

	public int getTripId() {
	    return tripId;
	}
	
	
	public L getFirstLocation() {
		return locations[currentFirstLocationIndex];
	}
	
	public L removeFirstLocation() {
		return locations[currentFirstLocationIndex++];
	}
	
	public L getLastLocation() {
		return locations[locations.length - 1];
	}
	
	public boolean isEmpty() {
		return locations.length == currentFirstLocationIndex;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Trip of type + ").append(locations[0].getClass());
		sb.append("[");
		String locString = Arrays.stream(locations).map(String::valueOf).collect(Collectors.joining(", "));
		sb.append(locString).append("]");
		return sb.toString();
	}
	
	public String locationsToString() {
		String str = "";

		for (L location : locations) {
			str += location.toString() + System.getProperty("line.separator");
		}

		return str;
	}
	
	public int getSize(){
		return locations.length - currentFirstLocationIndex;
	}
		
	private void checkLocations(L[] locations) throws TripException {
		for (L location : locations) {
			if (location == null) {
				throw new TripException();
			}
		}
	}
	
}
