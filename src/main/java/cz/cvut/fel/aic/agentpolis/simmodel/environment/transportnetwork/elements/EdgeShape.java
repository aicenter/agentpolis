package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EdgeShape implements Iterable<GPSLocation> {

    private final List<GPSLocation> backingMap;
    private double[] segmentAngles;
    private double visualLength;

    public EdgeShape(List<GPSLocation> backingMap) {
        if (backingMap == null || backingMap.size() < 2)
            throw new IllegalArgumentException();
        this.backingMap = backingMap;
    }


    public GPSLocation from() {
        return backingMap.get(0);
    }

    public GPSLocation to() {
        return backingMap.get(backingMap.size()-1);
    }

    public int size() {
        return backingMap.size();
    }

    public GPSLocation get(int i) {
        return backingMap.get(i);
    }

    public double getVisualLength() {
        return visualLength;
    }

    public void setVisualLength(double visualLength) {
        this.visualLength = visualLength;
    }

    public void setSegmentAngles(double[] angles) {
        if (this.segmentAngles == null)
            this.segmentAngles = new double[backingMap.size()-1];
        this.segmentAngles = angles;
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
}
