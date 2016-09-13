package cz.agents.agentpolis.simulator.visualization.googleearth.factories.density.circle;

import java.awt.Color;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.density.IDensityGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.apgooglearth.updates.density.CircleDensityUpdateGE;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simulator.visualization.googleearth.factories.density.AgentDensityUpdateGEFactory;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;

/**
 * 
 * The factory for the initialization of agent density visualization as circle
 * for GE
 * 
 * @author Zbynek Moler
 * 
 */
public class AgentCircleDensityUpdateGEFactory extends AgentDensityUpdateGEFactory {

    public AgentCircleDensityUpdateGEFactory(CameraAltVisibility cameraAltVisibility, Color color,
            Set<EntityType> allowedEntityType, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, color, allowedEntityType, nameOfUpdateKmlView);
    }

    @Override
    protected UpdateKmlView createDensityUpdateGE(IDensityGE densityGE, RegionBounds regionBounds,
            Color color) {
        return new CircleDensityUpdateGE(densityGE, regionBounds, color);
    }

}
