package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.region;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid2d.Trajectory;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Point4d;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Region;

import javax.vecmath.Point2d;

/**
 * Created by wmatex on 4.4.15.
 */
public class MovingCircle implements Region {
    private static final double DEFAULT_SAMPLES_PER_RADIUS = 2d;
    private static final double TOLERANCE = 0.01d;
    private double samplingInterval;
    Trajectory trajectory;
    double radius;

    public MovingCircle(Trajectory trajectory, double radius) {
        super();
        assert(trajectory != null);
        this.trajectory = trajectory;
        this.radius = radius;
        this.samplingInterval = radius/DEFAULT_SAMPLES_PER_RADIUS;
    }

    public MovingCircle(Trajectory trajectory, double radius, double samplingInterval) {
        super();
        this.trajectory = trajectory;
        this.radius = radius;
        this.samplingInterval = samplingInterval;
    }

    @Override
    public boolean intersectsLine(Point4d p1, Point4d p2) {
//        return trajectory.get(p2.getTime()).getPosition().distance(p2.getPosition()) < 2*radius;
        Point4d start;
        Point4d end;

        if (p1.getTime() < p2.getTime()) {
            start = p1;
            end = p2;
        } else {
            start = p2;
            end = p1;
        }



        return intersectsLineNumeric(start, end);
    }

	protected boolean intersectsLineNumeric(Point4d start, Point4d end) {
		double tmin = Math.max(trajectory.getMinTime(), start.getTime());
        double tmax = Math.min(trajectory.getMaxTime(), end.getTime());


//        return trajectory.get(end.getTime()).getPosition().distance(end.getPosition()) < 2*radius;
        for (double t = tmin; t <= tmax; t += samplingInterval) {
            // todo what if end - start == 0???
            double alpha = 0;
            if (end.getTime() - start.getTime() > 0) {
                alpha = (t - start.getTime()) /(end.getTime() - start.getTime());
            } else if (end.getTime() - start.getTime() == 0) {
                alpha = 0;
                assert(start.equals(end));
            } else {
                throw new RuntimeException();
            }

            assert (alpha >= -0.00001 && alpha <= 1.00001);

            tt.euclid2d.Point pos2d = tt.euclid2d.Point.interpolate(
                    new tt.euclid2d.Point(start.x, start.y),
                    new tt.euclid2d.Point(end.x, end.y), alpha);

            Point2d trajPoint = trajectory.get(t).getPosition();

            if (trajPoint.distance(pos2d) < 2*radius-TOLERANCE) {
                return true;
            }
        }

        return false;
	}

    @Override
    public boolean isInside(Point4d p) {
        if (p.getTime() >= trajectory.getMinTime() && p.getTime() <= trajectory.getMaxTime()) {
            return (p.getPosition().distance(trajectory.get(p.getTime()).getPosition()) <= radius);
        } else {
            return false;
        }

    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "MC(" + Integer.toHexString(trajectory.hashCode()) + ", " + radius + ")";
    }

    public double getSamplingInterval() {
		return samplingInterval;
	}
}
