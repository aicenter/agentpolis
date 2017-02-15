package cz.agents.agentpolis.simmodel;

import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import java.util.logging.Level;

/**
 * 
 * The agents in a simulation model are extended by this an abstract
 * representation.
 * 
 * 
 * @author Zbynek Moler
 */
public abstract class Agent extends AgentPolisEntity {

    private final EntityType type;
    
    /**
	 * Current chosen activity.
	 */
    Activity currentActivity;
    

    public Agent(final String agentId, final EntityType agentType) {
        super(agentId);
        this.type = agentType;
    }

    public void born(){
        
    }
	
	public void die(){
		
	};

    public EntityType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Agent " + getType().toString() + " " + getId();
    }
	
	protected void exit(){
		SimulationCreator.removeAgentStatic(this);
	}
    
    /**
	 * Called when current activity finises.
	 * @param activity Activity that just finished.
	 */
	protected void onActivityFinish(Activity activity) {
        Log.log(this, Level.FINE, "Activity finished: {0}", currentActivity);
    }
	
	/**
	 * Called when current activity fails.
	 * @param activity Activity that just failed.
	 * @param reason Reason of the failure.
	 */
	protected void onActionFailed(Activity activity, String reason) {
        Log.log(this, Level.SEVERE, "Activity {0} failed: {1}", currentActivity, reason);
    }

}
