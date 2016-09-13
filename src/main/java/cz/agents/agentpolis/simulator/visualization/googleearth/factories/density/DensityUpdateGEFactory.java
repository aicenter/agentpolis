package cz.agents.agentpolis.simulator.visualization.googleearth.factories.density;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Injector;

import cz.agents.agentpolis.apgooglearth.density.IDensityGE;
import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simulator.visualization.googleearth.UpdateGEFactory;
import cz.agents.agentpolis.simulator.visualization.googleearth.entity.DensityGE;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;
import cz.agents.alite.googleearth.updates.UpdateKmlView;

/**
 * 
 * The factory for the initialization of graph visualization for GE
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class DensityUpdateGEFactory extends UpdateGEFactory {

    private final Color color;
    private final Set<EntityType> allowedEntityType;

    public DensityUpdateGEFactory(CameraAltVisibility cameraAltVisibility, Color color,
            Set<EntityType> allowedEntityType, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, nameOfUpdateKmlView);
        this.color = color;
        this.allowedEntityType = allowedEntityType;

    }

    public final UpdateKmlView createUpdateKmlView(Injector injector, RegionBounds regionBounds) {

        EntityStorage<?> entityStorage = getEntityStorage(injector);
        List<String> entityIds = new ArrayList<String>(entityStorage.getEntityIds());

        Set<String> allowedEntitiesIds = new HashSet<String>();

        for (String entityId : entityIds) {
            AgentPolisEntity entity = entityStorage.getEntityById(entityId);
            if (allowedEntityType.contains((entity.getType()))) {
                allowedEntitiesIds.add(entityId);
            }
        }

        IDensityGE densityGE = new DensityGE(getEntityPositionStorage(injector), injector
                .getInstance(AllNetworkNodes.class).getAllNetworkNodes(), allowedEntitiesIds);

        return createDensityUpdateGE(densityGE, regionBounds, color);
    }

    protected abstract UpdateKmlView createDensityUpdateGE(IDensityGE densityGE,
            RegionBounds regionBounds, Color color);

    protected abstract EntityStorage<?> getEntityStorage(Injector injector);

    protected abstract EntityPositionModel getEntityPositionStorage(Injector injector);

}
