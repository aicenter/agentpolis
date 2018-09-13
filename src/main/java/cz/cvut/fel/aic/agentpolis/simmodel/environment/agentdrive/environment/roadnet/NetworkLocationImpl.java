package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.network.NetworkLocation;

import javax.vecmath.Point2f;
import java.awt.geom.Rectangle2D;

public class NetworkLocationImpl implements NetworkLocation {


    private Point2f offset;
    private Rectangle2D convertedBoundary;
    private Rectangle2D origBoundary;
    private String projection;

    public NetworkLocationImpl(Point2f offset, Rectangle2D convertedBoundary, Rectangle2D origBoundary, String projection) {
        this.offset = offset;
        this.convertedBoundary = convertedBoundary;
        this.origBoundary = origBoundary;
        this.projection = projection;
    }

    @Override
    public Point2f getOffset() {
        return offset;
    }

    @Override
    public Rectangle2D getConvertedBoundary() {
        return convertedBoundary;
    }

    @Override
    public Rectangle2D getOrigBoundary() {
        return origBoundary;
    }

    @Override
    public String getProjection() {
        return projection;
    }
}
