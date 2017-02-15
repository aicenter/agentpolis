package cz.agents.agentpolis.simmodel.entity;

import cz.agents.agentpolis.siminfrastructure.description.Description;
import cz.agents.alite.common.entity.Entity;
import cz.agents.basestructures.Node;

/**
 * Entity for AgentPolis (agent or vehicle ...)
 * 
 * @author Zbynek Moler
 * */
public abstract class AgentPolisEntity extends Entity implements Description {
    
    private final String id;
    
    private Node position;

    public Node getPosition() {
        return position;
    }

    public void setPosition(Node position) {
        this.position = position;
    }
    
    
    
    
    public AgentPolisEntity(String id) {
        super(id);
        
        this.id = id;        

    }

    public abstract EntityType getType();
    
    public String getId(){
        return id;
    }

}
