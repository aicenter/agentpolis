package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network;


import javax.vecmath.Point2f;
import java.awt.geom.Rectangle2D;

public interface NetworkLocation {
    Point2f getOffset();

    Rectangle2D getConvertedBoundary();

    Rectangle2D getOrigBoundary();

    String getProjection();
}
