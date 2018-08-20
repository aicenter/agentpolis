package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import java.util.Collection;
import java.util.LinkedList;

public class RadarData {
	
	private final Collection<RoadObject> cars = new LinkedList<RoadObject>();
	
	public void add(RoadObject car){
		this.cars.add(car);
	}
	
	public Collection<RoadObject> getCars() {
		return cars;
	}

	@Override
	public String toString() {
		return "RadarData [cars=" + cars + "]";
	}

}
