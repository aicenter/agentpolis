package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network;

import tt.euclid2d.region.Rectangle;

import javax.vecmath.Point2f;

public interface NetworkLocation {
    Point2f getOffset();

    Rectangle getConvertedBoundary();

    Rectangle getOrigBoundary();

    String getProjection();
}
