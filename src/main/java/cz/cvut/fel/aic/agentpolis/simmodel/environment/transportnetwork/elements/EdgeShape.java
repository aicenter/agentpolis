package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.AngleUtil;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EdgeShape implements Iterable<GPSLocation> {

    final List<GPSLocation> backingMap;
    double[] segmentAngles;
    double[] segmentCumulativeLength;
    double shapeLength;

    public EdgeShape(List<GPSLocation> backingMap) {
        if (backingMap == null || backingMap.size() < 2)
            throw new IllegalArgumentException();
        this.backingMap = backingMap;
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

    public double getShapeLength() {
        return shapeLength;
    }
}
