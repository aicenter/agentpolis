package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.factories.density;

import java.awt.Color;
import java.util.Set;

import com.google.inject.Injector;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehicleStorage;
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
//        return injector.getInstance(VehiclePositionModel.class);
        return null;
    }

    @Override
    protected EntityStorage<PhysicalVehicle> getEntityStorage(Injector injector) {
        return injector.getInstance(VehicleStorage.class);
    }

}
