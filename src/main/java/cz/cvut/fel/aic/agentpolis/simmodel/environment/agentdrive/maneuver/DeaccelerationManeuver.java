package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver;


import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;

public class DeaccelerationManeuver extends CarManeuver {
    public DeaccelerationManeuver(CarManeuver pred) {
        this(pred.getLaneOut(), pred.getVelocityOut(), pred.getPositionOut(),
                pred.getEndTime());

    }

    public DeaccelerationManeuver(int laneIn, double velocityIn,
                                  double positionIn, long startTime) {
        super(laneIn, velocityIn, positionIn, startTime);
        laneOut = laneIn;
        expectedLaneOut = laneIn;
        duration = AgentDriveModel.adConfig.deaccelerationManueverDuration;
        acceleration = AgentDriveModel.adConfig.deacceleration;
        velocityOut = velocityIn + (acceleration * duration);
        if (velocityOut < 0) {
            velocityOut = 0;
            double durationUntilStop = -velocityIn / acceleration;
            positionOut = positionIn + velocityIn * durationUntilStop + 0.5 * acceleration
                    * (Math.pow(durationUntilStop, 2));
        } else {
            positionOut = positionIn + velocityIn * duration + 0.5 * acceleration
                    * (Math.pow(duration, 2));
        }

    }


}
