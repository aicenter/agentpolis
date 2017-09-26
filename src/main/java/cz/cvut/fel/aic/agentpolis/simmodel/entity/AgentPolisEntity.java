package cz.cvut.fel.aic.agentpolis.simmodel.entity;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.typed.AliteEntity;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;

/**
 * Entity for AgentPolis (agent or vehicle ...)
 *
 * @author Zbynek Moler
 */
public abstract class AgentPolisEntity extends AliteEntity {

    private final String id;

    private SimulationNode position;

    public SimulationNode getPosition() {
        return position;
    }

    public void setPosition(SimulationNode position) {
        this.position = position;
    }

    public AgentPolisEntity(String id, SimulationNode position) {
        this.id = id;
        this.position = position;
    }

    @Inject
    @Override
    public void init(TypedSimulation eventProcessor) {
        super.init(eventProcessor);
    }


    public abstract EntityType getType();

    public String getId() {
        return id;
    }

}
