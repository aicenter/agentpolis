/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.alite.common.event.Event;

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

    public Agent(final String agentId, SimulationNode position) {
        super(agentId, position);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void born() {
    }

    public void die() {
    }


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
        Log.log(this, Level.FINER, "Activity finished: {0}", currentActivity);
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
