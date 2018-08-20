package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d;

/**
 * Region in the euclidean time-space
 * Created by wmatex on 4.4.15.
 */
public interface Region {
    public boolean isInside(Point4d point);
    public boolean intersectsLine(Point4d point1, Point4d point2);
}
