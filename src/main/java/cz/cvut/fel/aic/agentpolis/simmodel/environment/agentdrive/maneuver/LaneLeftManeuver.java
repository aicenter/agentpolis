package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;

import cz.agents.alite.configurator.Configurator;


public class LaneLeftManeuver extends CarManeuver {
	public LaneLeftManeuver(CarManeuver pred) {
		this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(),
				pred.getEndTime());
	
	}

	public LaneLeftManeuver(int laneIn, double velocityIn, double positionIn, long startTime) {
		super(laneIn, velocityIn, positionIn, startTime);

		laneOut = laneIn + 1;
		expectedLaneOut = laneIn + 1;
		duration = Configurator.getParamDouble("highway.safeDistanceAgent.maneuvers.laneChangeManeuverDuration", 2.0);
		velocityOut = velocityIn;
		acceleration = 0;
		positionOut = positionIn + velocityIn * duration;

	}
}
