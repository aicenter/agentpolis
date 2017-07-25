package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.moving;


/**
 * Utility for support movement. - It computes duration
 *
 * @author Zbynek Moler
 */
public final class MoveUtil {

    private MoveUtil() {
    }

    /**
     * Returns the time required for traveling for the given distance
     * with specified velocity.
     *
     * @param velocityInmps Velocity in metres per second.
     * @param lengthInMeter Distance in metres per second.
     * @return Required time in milliseconds.
     * @note (FIXME) The result is in different units (milliseconds) than inputs
     * (metres per second and metres); this method should be deprecated.
     */
    public static long computeDuration(double velocityInmps, double lengthInMeter) {

        double velocityInmpms = velocityInmps / 1000;

        // Compute duration on edge
        long duration = Math.round(lengthInMeter / velocityInmpms);

        // Minimal duration
        if (duration < 1) {
            duration = 1;
        }

        return duration;

    }

    public static double computeAgentOnEdgeVelocity(double driverMaximalVelocity, float allowedMaxSpeedOnRoad) {
        return Double.min(driverMaximalVelocity, allowedMaxSpeedOnRoad);
    }
}
