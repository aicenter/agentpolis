package cz.agents.agentpolis.siminfrastructure.description;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Servers for wrapping some information e.g about agent or agents life cycle.
 * 
 * @author Zbynek Moler
 *
 */
public class DescriptionImpl {

    private final List<String[]> descriptions = new ArrayList<String[]>();
    
    public DescriptionImpl() {
        
    }
    
    public void addDescriptionLine(String ... description){
        descriptions.add(description);
    }

    public void addDescriptionLine(DescriptionImpl description){
        descriptions.addAll(description.getDescriptions());
    }
    
    public List<String[]> getDescriptions() {
        return descriptions;
    }
    
    
    
    
}
