package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;
import java.util.ArrayList;

public interface Lane {

    String getLaneId();

    int getIndex();

    Edge getParentEdge();

    ArrayList<Point2f> getShape();


    Vector2f getCenter();

    float getLength();

    float getSpeed();

    ArrayList<Point2f> getInnerPoints();

    ArrayList<Lane> getIncomingLanes();

    ArrayList<Lane> getOutgoingLanes();

    Lane getLaneLeft();

    Lane getLaneRight();

    Lane getNextLane(Edge edge);

    Lane getPreviousLane(Edge edge);
}
