package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;
import cz.cvut.fel.aic.geographtools.util.Transformer;

@Singleton

public class ShapeUtils {
    private final Transformer transformer;

    @Inject
    public ShapeUtils(Transformer transformer) {
        this.transformer = transformer;
    }

    public PositionAndAngle getPositionAndAngleOnPath(EdgeShape shape, double portion) {
        assert portion >= 0. && portion <= 1.0;
        double distance = portion * shape.shapeLength;
        int segment = 0;
        while (shape.segmentCumulativeLength[segment] < distance) {
            segment++;
        }
        double distanceOnPreviousSegments = (segment > 0 ? shape.segmentCumulativeLength[segment - 1] : 0.0);
        double distanceOnSegment = distance - distanceOnPreviousSegments;
        double segmentLength = shape.segmentCumulativeLength[segment] - distanceOnPreviousSegments;
        double segmentPortion = distanceOnSegment / segmentLength;
        GPSLocation pointOnPath = getPointOnVector(shape.backingMap.get(segment), shape.backingMap.get(segment + 1), segmentPortion);
        double angle = shape.segmentAngles[segment];

        return new PositionAndAngle(pointOnPath, angle);
    }

    private GPSLocation getPointOnVector(GPSLocation gps1, GPSLocation gps2, double portion) {
        int xIncrement = (int) Math.round((gps2.getLongitudeProjected1E2()
                - gps1.getLongitudeProjected1E2()) * portion);
        int yIncrement = (int) Math.round((gps2.getLatitudeProjected1E2()
                - gps1.getLatitudeProjected1E2()) * portion);

        int latProjected = gps1.getLatitudeProjected1E2() + yIncrement;
        int lonProjected = gps1.getLongitudeProjected1E2() + xIncrement;
        int elevation = 0;
        return GPSLocationTools.createGPSLocationFromProjected(latProjected, lonProjected, elevation, transformer);
    }


    public class PositionAndAngle {
        public final GPSLocation point;
        public final double angle;

        public PositionAndAngle(GPSLocation pointOnPath, double angle) {
            this.point = pointOnPath;
            this.angle = angle;
        }
    }
}
