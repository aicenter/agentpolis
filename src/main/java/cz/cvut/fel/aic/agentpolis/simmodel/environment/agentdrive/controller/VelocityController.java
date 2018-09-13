package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.vehicle.Vehicle;

import java.util.Collection;

/**
 * Controller which scales vehicle velocity based on a acceleration
 * Created by wmatex on 18.7.14.
 */
public class VelocityController {
    private final static float DELTA_TIME = 1f;
    private final static float MAX_ACCELERATION = 4f;
    private final static float EPSILON = 0.1f;
    private float acceleration;
    private float desiredVelocity;
    private final Vehicle vehicle;

    public VelocityController(Vehicle vehicle) {
        acceleration = 0f;
        this.vehicle = vehicle;
    }

    /**
     * Updates acceleration based on a plan
     */
    public void updatePlan(Collection<Action> plan) {
        for (Action action : plan) {
            if (action instanceof WPAction) {
                if (((WPAction) action).getSpeed() == -1) {
                    setAcceleration(vehicle.getVelocity());
                }
                WPAction wpAction = (WPAction) action;
                setAcceleration((float) wpAction.getSpeed());
            }
        }
    }

    /**
     * Sets acceleration based on vehicle's current velocity and a desired velocity,
     * that the vehicle should gain in time
     */
    private void setAcceleration(float desiredVelocity) {
        this.desiredVelocity = desiredVelocity;
        float acc = (desiredVelocity - vehicle.getVelocity()) / DELTA_TIME;
        if (Math.abs(acc) > MAX_ACCELERATION) {
            acc = Math.signum(acc) * MAX_ACCELERATION;
        }

        acceleration = acc;
    }

    /**
     * Update the vehicle's velocity based on the acceleration. If the vehicle gains the desired
     * velocity, the acceleration is set to 0
     */
    public void updateVelocity(long deltaTime) {
        if (acceleration != 0f) {
            float currentVelocity = vehicle.getVelocity();
            float newVelocity = currentVelocity + acceleration * deltaTime / 1000f;
            // If the difference between the current velocity and the desired velocity is smaller
            // than some constant, stop accelerating
            if (Math.abs(newVelocity - desiredVelocity) < EPSILON) {
                vehicle.setVelocity(desiredVelocity);
                acceleration = 0f;
            } else {
                vehicle.setVelocity(newVelocity);
            }
        }
    }
}
