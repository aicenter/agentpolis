package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;

import cz.agents.alite.configurator.Configurator;

public class AccelerationManeuver extends CarManeuver {
    double maximalSpeed = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.maximalSpeed", 20.0);
    
	public AccelerationManeuver(CarManeuver pred) {
		this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(), pred.getEndTime());
	}

	public AccelerationManeuver(int laneIn, double velocityIn, double positionIn, long startTime) {
		super(laneIn, velocityIn, positionIn, startTime);
		laneOut = laneIn;
		expectedLaneOut = laneIn;
		duration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.accelerationManeuverDuration", 1.0);
		acceleration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.acceleration", 3.0);
		velocityOut = velocityIn + (acceleration * duration);

		if (velocityOut > maximalSpeed){
			velocityOut = maximalSpeed;
			double durationUntilMaxSpeed = (velocityOut - velocityIn) / acceleration;
			positionOut = positionIn + velocityIn * durationUntilMaxSpeed + 0.5 * acceleration
							* (Math.pow(durationUntilMaxSpeed,2))
							+ velocityOut * (duration - durationUntilMaxSpeed);
		}
		else {
		    positionOut = positionIn + velocityIn * duration + 0.5 * acceleration * (Math.pow(duration, 2));
		}
	}
}
