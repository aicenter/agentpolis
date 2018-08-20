package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid3d.SpeedPoint;
import tt.euclid2d.Point;

import javax.vecmath.Point2d;

/**
 * Class representing a SpeedPoint in time-extended space
 * Created by wmatex on 4.4.15.
 */
public class Point4d extends javax.vecmath.Point4d {
    public Point4d(double x, double y, double speed, double time) {
        super(x, y, speed, time);
    }

    public Point4d(Point2d spatial, double speed, double time) {
        super(spatial.x, spatial.y, speed, time);
    }

    public Point4d(SpeedPoint point, double time) {
        super(point.x, point.y, point.z, time);
    }

    public SpeedPoint getSpeedPoint() {
        return new SpeedPoint(x,y,z);
    }

    public Point getPosition() {
        return new Point(x,y);
    }

    public double getSpeed() {
        return z;
    }

    public double getTime() {
        return w;
    }

//    @Override
//    public String toString() {
//        return "[" + x + "," + y + "," + z + "," + w;
//    }
}
