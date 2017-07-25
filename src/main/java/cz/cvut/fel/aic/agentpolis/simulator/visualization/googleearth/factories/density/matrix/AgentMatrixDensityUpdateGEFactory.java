package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.factories.density.matrix;

import java.awt.Color;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.density.IDensityGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.density.MatrixDensityUpdateGE;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.factories.density.AgentDensityUpdateGEFactory;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;

/**
 * 
 * The factory for the initialization of agent density visualization as matrix
 * for GE
 * 
 * @author Zbynek Moler
 * 
 */
public class AgentMatrixDensityUpdateGEFactory extends AgentDensityUpdateGEFactory {

    public AgentMatrixDensityUpdateGEFactory(CameraAltVisibility cameraAltVisibility, Color color,
            Set<EntityType> allowedEntityType, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, color, allowedEntityType, nameOfUpdateKmlView);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected UpdateKmlView createDensityUpdateGE(IDensityGE densityGE, RegionBounds regionBounds,
            Color color) {
        return new MatrixDensityUpdateGE(densityGE, regionBounds, color);
    }

}
