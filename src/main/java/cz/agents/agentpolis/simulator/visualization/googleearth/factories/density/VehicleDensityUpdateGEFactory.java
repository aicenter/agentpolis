package cz.agents.agentpolis.simulator.visualization.googleearth.factories.density;

import java.awt.Color;
import java.util.Set;

import com.google.inject.Injector;

import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.agents.agentpolis.simmodel.environment.model.VehicleStorage;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;

/**
 * 
 * The factory for the initialization of vehicle density visualization for GE
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class VehicleDensityUpdateGEFactory extends DensityUpdateGEFactory {

    public VehicleDensityUpdateGEFactory(CameraAltVisibility cameraAltVisibility, Color color,
            Set<EntityType> allowedEntityType, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, color, allowedEntityType, nameOfUpdateKmlView);
    }

    @Override
    protected EntityPositionModel getEntityPositionStorage(Injector injector) {
        return injector.getInstance(VehiclePositionModel.class);
    }

    @Override
    protected EntityStorage<Vehicle> getEntityStorage(Injector injector) {
        return injector.getInstance(VehicleStorage.class);
    }

}
