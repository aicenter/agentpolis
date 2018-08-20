package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid3d;

import javax.vecmath.*;

/**
 * Class representing a point in 2D space with a speed attached
 * Created by wmatex on 4.4.15.
 */
public class SpeedPoint extends Point3d {
    public SpeedPoint() {
        super();
    }

    public SpeedPoint(Point2d spatial, double speed) {
        super(spatial.x, spatial.y, speed);
    }

    public SpeedPoint(double x, double y, double speed) {
        super(x,y,speed);
    }

    public Point2d getPosition() {
        return new Point2d(x,y);
    }

    public double getSpeed() {
        return z;
    }
}
