package cz.agents.agentpolis.simmodel.environment.model.entityvelocitymodel;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.inject.Singleton;

/**
 * This class takes information about entity velocity.
 * Based on this information is computed entity traveling time 
 * 
 * @author Zbynek Moler
 *
 */
@Singleton
public class EntityVelocityModel{

    /**
     * Minimum value of velocity considered "sane"
     * (currently 0.01 millimeter per second).
     */
    public final double MIN_SANE_VELOCITY = 0.00001;
    
    private final Map<String,Double> maxSpeedPerEntity;

    public EntityVelocityModel() {
        super();
        this.maxSpeedPerEntity = new HashMap<>();
    }
    
    
    public void addEntityMaxVelocity(String entityId, Double velocityInmps){
        if (velocityInmps < MIN_SANE_VELOCITY) {
            throw new IllegalArgumentException("Velocity is too small (<0.01 mm/s) for entity " + entityId);
        }
        maxSpeedPerEntity.put(entityId, velocityInmps);
    }
	
	public void removeEntityMaxVelocity(String entityId){
        maxSpeedPerEntity.remove(entityId);
    }
    
    /**
     * Returns the maximum velocity for the given entity.
     * @param entityId Identifier of the entity.
     * @return Maximum speed in metres per second.
     * @exception NoSuchElementException There were no data found for the specified
     * entity; this usually means that the database of vehicles is incomplete.
     */
    public Double getEntityVelocityInmps(String entityId){
        Double result = maxSpeedPerEntity.get(entityId);
        if (result == null) {
            throw new NoSuchElementException("Velocity data not found for entity " + entityId);
        }
        return result;
    }
    
    
    
}
