package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.RoadNetwork;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;
import java.util.ArrayList;
import java.util.List;

public class RoadDescription {


    private final RoadNetwork roadNetwork;
    private final List<Line> lines = new ArrayList<Line>();
    private final List<RoadObject> obstacles = new ArrayList<RoadObject>();

    public RoadDescription(RoadNetwork roadNetwork) {
        this.roadNetwork = roadNetwork;
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    private double distance(Point3d p1, Point3d p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }


    public List<Line> getLines() {
        return lines;
    }

    public List<RoadObject> getObstacles() {
        return obstacles;
    }

    public double distance(Point2d position) {
        return Math.sqrt(Math.pow(position.y, 2) * Math.pow(position.x, 2));
    }

    public double distance(Point2d point2d, Point2f edgeBeginPoint) {
        return Math.sqrt(Math.pow(point2d.x - edgeBeginPoint.x, 2) + Math.pow(point2d.y - edgeBeginPoint.y, 2));
    }

    public static class Line {
        public final Point3d a;
        public final Point3d b;

        public Line(Point3d a, Point3d b) {
            this.a = a;
            this.b = b;
        }

        public boolean isTaper() {
            return a.z > b.z;
        }

        public boolean isExtension() {
            return a.z < b.z;
        }
    }

}
