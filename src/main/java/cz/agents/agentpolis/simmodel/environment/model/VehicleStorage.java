package cz.agents.agentpolis.simmodel.environment.model;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;

/**
 * 
 * The storage of all initialized vehicles in simulation model
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class VehicleStorage extends EntityStorage<PhysicalVehicle> {

    @Inject
    public VehicleStorage() {
        super();
    }

}
