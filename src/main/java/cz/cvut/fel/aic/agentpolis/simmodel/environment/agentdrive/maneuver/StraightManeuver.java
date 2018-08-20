package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;

import cz.agents.alite.configurator.Configurator;


public class StraightManeuver extends CarManeuver {
	public StraightManeuver(int laneIn, double velocityIn, double positionIn,
			long startTime) {
		super(laneIn, velocityIn, positionIn, startTime);
		laneOut = laneIn;
		expectedLaneOut = laneIn;
		duration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.straightManeuverDuration", 1.0);;
		velocityOut = velocityIn;
		acceleration = 0;
		positionOut = positionIn + velocityIn * duration;
	}

	public StraightManeuver(CarManeuver pred) {
		this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(),
				pred.getEndTime());
	}

	

}
