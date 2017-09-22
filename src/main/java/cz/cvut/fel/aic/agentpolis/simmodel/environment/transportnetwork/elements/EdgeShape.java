package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.AngleUtil;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;
import cz.cvut.fel.aic.geographtools.util.Transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EdgeShape implements Iterable<GPSLocation> {

    private final List<GPSLocation> backingMap;
    private double[] segmentAngles;
    private double[] segmentCumulativeLength;
    private double shapeLength;
    private Transformer transformer;

    @Inject
    private EdgeShape(@Assisted List<GPSLocation> backingMap, Transformer transformer) {
        if (backingMap == null || backingMap.size() < 2)
            throw new IllegalArgumentException();
        this.backingMap = backingMap;
        this.transformer = transformer;
        segmentAngles = new double[backingMap.size() - 1];
        segmentCumulativeLength = new double[backingMap.size() - 1];
        for (int i = 1; i < backingMap.size(); i++) {
            GPSLocation a = backingMap.get(i - 1);
            GPSLocation b = backingMap.get(i);
            shapeLength += GPSLocationTools.computeDistanceAsDouble(a, b);
            segmentCumulativeLength[i - 1] = shapeLength;
            segmentAngles[i - 1] = AngleUtil.computeAngle(a, b);
        }
    }


    public GPSLocation from() {
        return backingMap.get(0);
    }

    public GPSLocation to() {
        return backingMap.get(backingMap.size() - 1);
    }

    public int size() {
        return backingMap.size();
    }


    public double[] getSegmentAngles() {
        return segmentAngles;
    }

    @Override
    public Iterator<GPSLocation> iterator() {
        return backingMap.iterator();
    }

    @Override
    public void forEach(Consumer<? super GPSLocation> consumer) {
        backingMap.forEach(consumer);
    }

    @Override
    public Spliterator<GPSLocation> spliterator() {
        return backingMap.spliterator();
    }

    public Stream<GPSLocation> stream() {
        return backingMap.stream();
    }

    public GPSLocation getPositionOnPath(double portion) {
        assert portion >= 0. && portion <= 1.0;
        double distance = portion * shapeLength;
        int segment = 0;
        while (segmentCumulativeLength[segment] < distance) {
            segment++;
        }
        double distanceOnSegment = segmentCumulativeLength[segment - 1] - distance;
        double segmentLength = segmentCumulativeLength[segment] - segmentCumulativeLength[segment - 1];
        double segmentPortion = distanceOnSegment / segmentLength;
        return getPointOnVector(backingMap.get(segment - 1), backingMap.get(segment), segmentPortion);
    }

    private GPSLocation getPointOnVector(GPSLocation gps1, GPSLocation gps2, double portion) {
        int xIncrement = (int) Math.round((gps2.getLongitudeProjected1E2()
                - gps1.getLongitudeProjected1E2()) * portion);
        int yIncrement = (int) Math.round((gps2.getLatitudeProjected1E2()
                - gps1.getLatitudeProjected1E2()) * portion);

        int latProjected = gps1.getLatitudeProjected1E2() + yIncrement;
        int lonProjected = gps1.getLongitudeProjected1E2() + xIncrement;
        return posi(latProjected, lonProjected)
    }

    @Singleton
    static class EdgeShapeFactory {
        private final Transformer transformer;

        @Inject
        public EdgeShapeFactory(Transformer transformer) {
            this.transformer = transformer;
        }

        public EdgeShape create(List<GPSLocation> list) {
            return new EdgeShape(list);
        }
    }
}
