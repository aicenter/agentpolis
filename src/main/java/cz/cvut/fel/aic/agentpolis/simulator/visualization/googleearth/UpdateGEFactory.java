package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth;

import com.google.inject.Injector;

import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;

/**
 * 
 * The abstract factory for the initialization of {@code UpdateKmlView} an
 * providing the Implementation of {@code CameraAltVisibility} which enables to
 * show/hide GE visualization just for the particular altitude.
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class UpdateGEFactory {

    private final CameraAltVisibility cameraAltVisibility;
    private final String nameOfUpdateKmlView;

    public UpdateGEFactory(CameraAltVisibility cameraAltVisibility, String nameOfUpdateKmlView) {
        super();
        this.cameraAltVisibility = cameraAltVisibility;
        this.nameOfUpdateKmlView = nameOfUpdateKmlView;
    }

    public abstract UpdateKmlView createUpdateKmlView(Injector injector, RegionBounds regionBounds);

    public final String getNameUpdateKmlView() {
        return nameOfUpdateKmlView;
    }

    public final CameraAltVisibility getCameraAltVisibility() {
        return cameraAltVisibility;
    }
}
