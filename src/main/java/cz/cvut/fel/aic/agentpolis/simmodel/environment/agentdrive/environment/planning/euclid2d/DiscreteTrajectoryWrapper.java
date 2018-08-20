package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid2d;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid3d.SpeedPoint;
import tt.euclid2i.*;

import javax.vecmath.Point2d;
/**
 * Make continuous trajectory look like discrete one, to be able to integrate it easily with trajectory tools
 * Created by wmatex on 4.4.15.
 */
public class DiscreteTrajectoryWrapper implements tt.euclid2i.Trajectory {
    private static final int PRECISION = 10;
    private final Trajectory continuousTrajectory;
    public DiscreteTrajectoryWrapper(Trajectory trajectory) {
        continuousTrajectory = trajectory;
    }
    @Override
    public int getMinTime() {
        return (int) Math.ceil(continuousTrajectory.getMinTime());
    }

    @Override
    public int getMaxTime() {
        return (int) Math.floor(continuousTrajectory.getMaxTime());
    }

    @Override
    public Point get(int t) {
        SpeedPoint speedPoint = continuousTrajectory.get((double)t/(double) PRECISION);
        if (speedPoint != null) {
            Point2d point2d = speedPoint.getPosition();
            return new Point((int) Math.floor(point2d.x), (int) Math.floor(point2d.y));
        } else {
            return null;
        }
    }
}
