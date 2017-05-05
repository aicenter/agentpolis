package cz.agents.agentpolis.simmodel.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.description.Description;
import cz.agents.alite.common.entity.Entity;
import cz.agents.alite.common.event.typed.AliteEntity;
import cz.agents.alite.common.event.typed.TypedSimulation;
import cz.agents.basestructures.Node;

/**
 * Entity for AgentPolis (agent or vehicle ...)
 * 
 * @author Zbynek Moler
 * */
public abstract class AgentPolisEntity extends AliteEntity implements Description {
    
    private final String id;
    
    private Node position;

    public Node getPosition() {
        return position;
    }

    public void setPosition(Node position) {
        this.position = position;
    }
    
    
    
    
    public AgentPolisEntity(String id, Node position) {
        this.id = id;        
        this.position = position;
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
