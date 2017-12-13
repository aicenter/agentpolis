package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;

public class LaneCongestionModel {
    private AgentpolisConfig config;
    private TimeProvider timeProvider;

    @Inject
    public LaneCongestionModel(AgentpolisConfig config, TimeProvider timeProvider) {
        this.config = config;
        this.timeProvider = timeProvider;
    }

    public long computeTransferDelay(VehicleTripData vehicleTripData, Lane toLane) {

        if (config.congestionModel.fundamentalDiagramDelay) {
            return computeCongestedTransferDelay(vehicleTripData, toLane);
        } else {
            return computeFreeFlowTransferDelay(vehicleTripData.getVehicle());
        }
    }



    private double computeCongestedSpeed(double freeFlowVelocity, Lane lane) {
        double carsPerKilometer = lane.getDrivingCarsCountOnLane() / lane.link.edge.shape.getShapeLength() * 1000.0;

        double congestedSpeed;
        if (carsPerKilometer <= config.congestionModel.criticalDensity) {
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

    private long computeCongestedTransferDelay(VehicleTripData vehicleTripData, Lane lane) {
        double freeFlowSpeed = vehicleTripData.getVehicle().getVelocity();
        double congestedSpeed = computeCongestedSpeed(freeFlowSpeed, lane);
        return Math.round(vehicleTripData.getVehicle().getLength() * 1E3 / congestedSpeed);
    }

    public static long computeFreeFlowTransferDelay(PhysicalVehicle vehicle) {

        return Math.round(vehicle.getLength() * 1E3 / vehicle.getVelocity());
    }
}
