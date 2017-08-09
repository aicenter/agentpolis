package cz.cvut.fel.aic.agentpolis.simmodel.environment.entityvelocitymodel.query;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.entityvelocitymodel.EntityVelocityModel;

@Singleton
public class VehicleInfoQuery {

    private final EntityVelocityModel entityVelocityStorage;

    @Inject
    public VehicleInfoQuery(EntityVelocityModel entityVelocityStorage) {
        super();
        this.entityVelocityStorage = entityVelocityStorage;
    }

    /**
     * 
     * Provides the allowed speed for given vehicle
     * 
     * @param vehicleId
     * @return
     */
    public double getCurrrentVehicleVelocity(String vehicleId) {
        return entityVelocityStorage.getEntityVelocityInmps(vehicleId);
    }

}
