package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet;



import cz.cvut.fel.aic.alite.configurator.Configurator;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;
import java.util.ArrayList;

/**
 * Structure holding data about a lane loaded from sumo .net.xml file
 * Created by pavel on 19.6.14.
 */
public class LaneImpl implements Lane{
    public static final float INNER_POINTS_STEP_SIZE = Configurator.getParamDouble("simulator.net.lane.stepSize", 1d).floatValue();
    private final String laneId;
    private final int index;
    private final float speed;
    private final float length;
    private final ArrayList<Point2f> shape;
    private Edge edge;
    private Vector2f center;
    private ArrayList<Point2f> innerPoints;
    private ArrayList<Lane> incomingLanes = new ArrayList<Lane>();
    private ArrayList<Lane> outgoingLanes = new ArrayList<Lane>();

    public LaneImpl(String laneId, int index, float speed, float length, ArrayList<Point2f> shape) {
        this.laneId = laneId;
        this.index = index;
        this.speed = speed;
        this.length = length;
        this.shape = shape;
        computeLaneCenter();
        computeInnerPoints();
    }

    private void computeInnerPoints() {
        innerPoints = new ArrayList<Point2f>();
        for (int i = 0; i < shape.size() - 1; i++) {
            Point2f start = shape.get(i);
            Point2f end = shape.get(i + 1);
            float distance = 0;
            Point2f pOld = start;
            Vector2f direction = new Vector2f(end.x - start.x, end.y - start.y);
            direction.normalize();
            direction.scale(INNER_POINTS_STEP_SIZE);
            innerPoints.add(start);
            while (distance < start.distance(end)) {
                Point2f p = new Point2f(pOld);
                p.add(direction);
                innerPoints.add(p);
                distance += p.distance(pOld);
                pOld = p;
            }
        }
    }

    public Edge getParentEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    private void computeLaneCenter() {
        float x = 0, y = 0;
        for (Point2f point : shape) {
            x += point.x;
            y += point.y;
        }
        x = x / shape.size();
        y = y / shape.size();
        this.center = new Vector2f(x, y);
    }

    public String getLaneId() {
        return laneId;
    }

    public int getIndex() {
        return index;
    }

    public float getSpeed() {
        return speed;
    }

    public float getLength() {
        return length;
    }

    public ArrayList<Point2f> getShape() {
        return shape;
    }

    public ArrayList<Point2f> getInnerPoints() {
        return innerPoints;
    }

    public Vector2f getCenter() {
        return center;
    }

    public void addOutgoingLane(LaneImpl lane) {
        outgoingLanes.add(lane);
    }

    public void addIncomingLane(LaneImpl lane) {
        incomingLanes.add(lane);
    }

    public ArrayList<Lane> getIncomingLanes() {
        return incomingLanes;
    }

    public ArrayList<Lane> getOutgoingLanes() {
        return outgoingLanes;
    }

    /**
     * Return outgoing lane going through the given edge
     * @return null if no lane is found
     */
    public Lane getNextLane(Edge edge) {
        if(edge == null) return null;
        for (Lane lane: getOutgoingLanes()) {
            if (lane.getParentEdge().getId() == edge.getId()) {
                return lane;
            }
        }
        return null;
    }

    /**
     * Return incoming lane going through the given edge
     * @return null if no lane is found
     */
    public Lane getPreviousLane(Edge edge) {
        if(edge == null) return null;
        for (Lane lane: getIncomingLanes()) {
            if (lane.getParentEdge().getId() == edge.getId()) {
                return lane;
            }
        }
        return null;
    }

    /**
     * Returns the lane left of this one in the driving direction
     * @return null if this is the left-most lane
     */
    public Lane getLaneLeft() {
        return getParentEdge().getLaneByIndex(getIndex()+1);
    }

    /**
     * Returns the lane right of this one in the driving direction
     * @return null if this is the right-most lane
     */
    public Lane getLaneRight() {
        return getParentEdge().getLaneByIndex(getIndex()-1);
    }
}

