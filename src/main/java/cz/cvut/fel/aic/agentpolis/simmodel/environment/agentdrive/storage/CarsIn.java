package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import java.util.Collection;
import java.util.LinkedList;

public class CarsIn {
	
	private final Collection<CarIn> cars = new LinkedList<CarIn>();
	
	public void add(CarIn carIn){
		this.cars.add(carIn);
	}
	
	public Collection<CarIn> getCars() {
		return cars;
	}

	@Override
	public String toString() {
		return "CarsMessage [cars=" + cars + "]";
	}

}
