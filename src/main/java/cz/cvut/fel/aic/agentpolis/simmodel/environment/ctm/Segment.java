package cz.cvut.fel.aic.agentpolis.simmodel.environment.ctm;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;

public class Segment {
    SegmentQueue carQueue;
    double length;
    double v;
    double w;
    double criticalDensity;

    public Segment(SimulationEdge edge) {
        this.length = edge.shape.getShapeLength();
        this.v = edge.allowedMaxSpeedInMpS;
        this.w = edge.allowedMaxSpeedInMpS;
        this.criticalDensity = 0.02 * edge.getLanesCount();
        this.carQueue = new SegmentQueue();
    }
}
