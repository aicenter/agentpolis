package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;


import cz.cvut.fel.aic.agentpolis.config.ADModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;

public class LaneLeftManeuver extends CarManeuver {
	public LaneLeftManeuver(CarManeuver pred) {
		this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(),
				pred.getEndTime());
	
	}

	public LaneLeftManeuver(int laneIn, double velocityIn, double positionIn, long startTime) {
		super(laneIn, velocityIn, positionIn, startTime);

		laneOut = laneIn + 1;
		expectedLaneOut = laneIn + 1;
		duration = AgentDriveModel.adConfig.laneChangeManeuverDuration;
		velocityOut = velocityIn;
		acceleration = 0;
		positionOut = positionIn + velocityIn * duration;

	}
}
