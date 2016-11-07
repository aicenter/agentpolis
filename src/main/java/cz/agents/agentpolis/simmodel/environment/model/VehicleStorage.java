package cz.agents.agentpolis.simmodel.environment.model;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;

/**
 * 
 * The storage of all initialized vehicles in simulation model
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleStorage extends EntityStorage<Vehicle> {

    @Inject
    public VehicleStorage() {
        super();
    }

}
