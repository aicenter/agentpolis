package cz.agents.agentpolis.simulator.visualization.googleearth.factories.density;

import java.awt.Color;
import java.util.Set;

import com.google.inject.Injector;

import cz.agents.agentpolis.simmodel.agent.Agent;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.AgentStorage;
import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.alite.googleearth.cameraalt.visibility.CameraAltVisibility;

/**
 * 
 * The factory for the initialization of agent density visualization for GE
 * 
 * @author Zbynek Moler
 * 
 */
public abstract class AgentDensityUpdateGEFactory extends DensityUpdateGEFactory {

    public AgentDensityUpdateGEFactory(CameraAltVisibility cameraAltVisibility, Color color,
            Set<EntityType> allowedEntityType, String nameOfUpdateKmlView) {
        super(cameraAltVisibility, color, allowedEntityType, nameOfUpdateKmlView);
    }

    @Override
    protected EntityPositionModel getEntityPositionStorage(Injector injector) {
        return injector.getInstance(AgentPositionModel.class);
    }

    @Override
    protected EntityStorage<Agent> getEntityStorage(Injector injector) {
        return injector.getInstance(AgentStorage.class);
    }

}
