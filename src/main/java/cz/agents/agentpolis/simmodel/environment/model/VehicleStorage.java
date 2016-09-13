package cz.agents.agentpolis.simmodel.environment.model;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.agents.agentpolis.simmodel.entity.EntityType;
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
    public VehicleStorage(Map<String, Vehicle> entities, Map<EntityType, Set<String>> entitiesByType) {
        super(entities, entitiesByType);
    }

}
