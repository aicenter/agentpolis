package cz.agents.agentpolis.simmodel;

import cz.agents.agentpolis.siminfrastructure.Log;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.EntityType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.alite.common.event.Event;
import cz.agents.basestructures.Node;

import java.util.logging.Level;

/**
 * The agents in a simulation model are extended by this an abstract
 * representation.
 * <p>
 * Rewritten based on agent from ninja.fido.agentSCAI
 *
 * @author Zbynek Moler
 */
public abstract class Agent extends AgentPolisEntity {


    /**
     * Current chosen activity.
     */
    Activity currentActivity;


    public Activity getCurrentActivity() {
        return currentActivity;
    }


    public Agent(final String agentId, SimulationNode position) {
        super(agentId, position);
    }

    public void born() {

    }

    public void die() {

    }

    ;

    public abstract EntityType getType();

    @Override
    public String toString() {
        return "Agent " + getType().toString() + " " + getId();
    }

    @Override
    public void handleEvent(Event event) {
        currentActivity.handleEvent(event);
    }

    /**
     * Called when current activity finises.
     *
     * @param activity Activity that just finished.
     */
    protected void onActivityFinish(Activity activity) {
        Log.log(this, Level.FINE, "Activity finished: {0}", currentActivity);
    }

    /**
     * Called when current activity fails.
     *
     * @param activity Activity that just failed.
     * @param reason   Reason of the failure.
     */
    protected void onActionFailed(Activity activity, String reason) {
        Log.log(this, Level.SEVERE, "Activity {0} failed: {1}", currentActivity, reason);
    }

    public void processMessage(Message message) {
        if (currentActivity == null) {
            Log.warn(this, "Unprocessed message: " + message + ", agent has no current activity to process it!");
        } else {
            currentActivity.processMessage(message);
        }
    }
}
