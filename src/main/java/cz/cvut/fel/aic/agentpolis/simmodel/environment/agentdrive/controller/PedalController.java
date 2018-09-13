package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller.ControllerInterface;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.vehicle.Vehicle;

/**
 * Created by wmatex on 8.7.14.
 */
public class PedalController implements ControllerInterface {
    private static final float FORCE_CONSTANT = 1;
    @Override
    public void updateVehicleVelocity(Vehicle vehicle, long deltaTime, float steeringAngle, float pedalState) {
        // Pedal state should be in interval (-1;1)
        if (Math.abs(pedalState) < 1) {
            vehicle.setSteeringAngle(steeringAngle);

            // Apply acceleration based on the pedal state
            vehicle.scaleVelocity(1f+(pedalState * deltaTime / 1000f * FORCE_CONSTANT));
        }
    }
}
