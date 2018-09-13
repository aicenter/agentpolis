package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;


import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;

public class StraightManeuver extends CarManeuver {
	public StraightManeuver(int laneIn, double velocityIn, double positionIn,
			long startTime) {
		super(laneIn, velocityIn, positionIn, startTime);
		laneOut = laneIn;
		expectedLaneOut = laneIn;
		duration = AgentDriveModel.adConfig.straightManeuverDuration;
		velocityOut = velocityIn;
		acceleration = 0;
		positionOut = positionIn + velocityIn * duration;
	}

	public StraightManeuver(CarManeuver pred) {
		this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(),
				pred.getEndTime());
	}

	

}
