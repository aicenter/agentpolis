package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;

public class CongestionLaneModel {
    private AgentpolisConfig config;
    private TimeProvider timeProvider;

    @Inject
    public CongestionLaneModel(AgentpolisConfig config, TimeProvider timeProvider) {
        this.config = config;
        this.timeProvider = timeProvider;
    }

    /**
     * Called from Congestion model
     *
     * @param congestedTripData vehicle trip
     * @param toCongestionLane  target lane
     * @return ms
     */
    public long computeTransferDelay(CongestedTripData congestedTripData, CongestionLane toCongestionLane) {
        if (config.congestionModel.fundamentalDiagramDelay) {
            return computeCongestedTransferDelay(congestedTripData, toCongestionLane);
        } else {
            return computeFreeFlowTransferDelay(congestedTripData.getVehicle());
        }
    }

    // Free flow
    private static long computeFreeFlowTransferDelay(PhysicalVehicle vehicle) {
        return Math.round(vehicle.getLength() * 1E3 / vehicle.getVelocity());
    }

    // Congested
    private long computeCongestedTransferDelay(CongestedTripData congestedTripData, CongestionLane congestionLane) {
        double freeFlowSpeed = congestedTripData.getVehicle().getVelocity();
        double congestedSpeed = computeCongestedSpeed(freeFlowSpeed, congestionLane);
        return Math.round(congestedTripData.getVehicle().getLength() * 1E3 / congestedSpeed);
    }

    private double computeCongestedSpeed(double freeFlowVelocity, CongestionLane congestionLane) {
        double carsPerKilometer = congestionLane.getDrivingCarsCountOnLane() / congestionLane.parentLink.edge.shape.getShapeLength() * 1000.0;

        double congestedSpeed;
        if (carsPerKilometer < 20) {
            congestedSpeed = freeFlowVelocity;
        } else if (carsPerKilometer > 70) {
            congestedSpeed = 0.1 * freeFlowVelocity;
        } else {
            congestedSpeed = freeFlowVelocity * calculateSpeedCoefficient(carsPerKilometer);
        }
        Log.debug(this, "Congested speed: " + carsPerKilometer + "cars / km -> " + congestedSpeed + "m / s");

        return congestedSpeed;
    }

    private double calculateSpeedCoefficient(double carsPerKilometer) {
        //        WoframAlpha LinearModelFit[{{20, 100}, {30, 60}, {40, 40}, {70, 10}}, {x, x^2}, x]
        // interpolate speed for freeFlowSpeed = 100kmph
        //        0.0428177 x^2 - 5.61878 x + 193.757 (quadratic)
        double x = carsPerKilometer;
        double reducedSpeed = (0.0428177 * x * x - 5.61878 * x + 193.757);
        return reducedSpeed / 100.0;
    }
}
