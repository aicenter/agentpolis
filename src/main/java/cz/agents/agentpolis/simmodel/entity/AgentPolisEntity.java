package cz.agents.agentpolis.simmodel.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.description.Description;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.alite.common.event.typed.AliteEntity;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.GPSLocation;

/**
 * Entity for AgentPolis (agent or vehicle ...)
 * 
 * @author Zbynek Moler
 * */
public abstract class AgentPolisEntity extends AliteEntity implements Description {
    
    private final String id;
    
    private SimulationNode position;
    
    private GPSLocation precisePosition;
    

    
    
    public SimulationNode getPosition() {
        return position;
    }

    public void setPosition(SimulationNode position) {
        this.position = position;
        setPrecisePosition(position);
    }

    public GPSLocation getPrecisePosition() {
        return precisePosition;
    }

    public void setPrecisePosition(GPSLocation precisePosition) {
        this.precisePosition = precisePosition;
    }
    
    
    
    
    
    
    public AgentPolisEntity(String id, SimulationNode position) {
        this.id = id;        
        this.position = position;
        this.precisePosition = position;
    }

    @Inject
    @Override
    public void init(TypedSimulation eventProcessor) {
        super.init(eventProcessor); 
    }
    
    
    
    

    public abstract EntityType getType();
    
    public String getId(){
        return id;
    }

}
