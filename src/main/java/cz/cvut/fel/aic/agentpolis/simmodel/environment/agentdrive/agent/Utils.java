package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.ActualLanePosition;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Edge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.LaneImpl;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.List;

public class Utils {

    /**
     * Distance betwenn two road objects is calculated by finding two nearest waipoints of the cars and distance between them,
     * on the "same" line.
     *
     * @param me                      my road state
     * @param myActualLanePosition
     * @param other                   other's vehicle road state.
     * @param otherActualLanePosition
     * @return distance
     */
    public static double getDistanceBetweenTwoRoadObjects(RoadObject me, ActualLanePosition myActualLanePosition, RoadObject other, ActualLanePosition otherActualLanePosition, List<Edge> rem) {
        if (me.getId() == 809){
            System.out.println("w");
        }
        Lane myLane = myActualLanePosition.getLane();
        Lane otherLane = otherActualLanePosition.getLane();
        int nearestA = myActualLanePosition.getIndex();
        int nearestB = otherActualLanePosition.getIndex();

        Edge myEdg = myLane.getParentEdge();
        Edge his = otherLane.getParentEdge();
        double distance = 0;
        int maxSize = 0;
        if (myEdg.equals(his)) {
            if (myLane.getInnerPoints().size() < otherLane.getInnerPoints().size())  //two lines in the same edge with diferent number of waipoints, typicaly in curves
            {
                maxSize = myLane.getInnerPoints().size();
            } else {
                maxSize = otherLane.getInnerPoints().size();
            }
            if (nearestA < nearestB)// car is ahead
            {
                for (int i = nearestA + 1; i <= nearestB && i < maxSize; i++) {
                    distance += myLane.getInnerPoints().get(i - 1).distance(myLane.getInnerPoints().get(i));
                }
                return distance;
            } else if (nearestA > nearestB) {
                for (int i = nearestB + 1; i <= nearestA && i < maxSize; i++) {
                    distance += myLane.getInnerPoints().get(i - 1).distance(myLane.getInnerPoints().get(i));
                }
                return -distance;
            } else
                return 0;
        } else {
            // calculate distance to the vehicle on the edge that is on my plan.
            double distC = 0;
            for (int i = nearestA + 1; i < myLane.getInnerPoints().size(); i++) {
                distC += myLane.getInnerPoints().get(i - 1).distance(myLane.getInnerPoints().get(i));
            }
            boolean foundEdge = false;
            for (int i = 0; i < rem.size(); i++) {
                if (rem.get(i).equals(his)) {
                    foundEdge = true;
                    for (int d = 1; d < nearestB; d++) {
                        distC += otherLane.getInnerPoints().get(d - 1).distance(otherLane.getInnerPoints().get(d));
                    }

                    break;
                } else {
                    LaneImpl tem = rem.get(i).getLaneByIndex(0);
                    for (int d = 1; d < tem.getInnerPoints().size(); d++) {
                        distC += tem.getInnerPoints().get(d - 1).distance(tem.getInnerPoints().get(d));
                    }
                }
            }
            if (!foundEdge) return Double.MIN_VALUE;
            return distC;
        }
    }

    /**
     * Calculate distance between two points
     *
     * @param innerPoint
     * @param position
     * @return distance
     */
    public static float distanceP2P2(Point2f innerPoint, Point2f position) {
        return innerPoint.distance(position);
    }

    public static Point2f convertPoint3ftoPoint2f(Point3f point) {
        return new Point2f(point.x, point.y);
    }

    public static Point3f convertPoint2ftoPoint3f(Point2f point) {
        return new Point3f(point.x, point.y, 0);
    }

    public static Vector2f convertVector3ftoVector2f(Vector3f vec) {
        return new Vector2f(vec.x, vec.y);
    }

    /**
     * This method is for detection of intersection between two lane segments
     *
     * @param p0_x x coordinate of the starting point of the first lane segment
     * @param p0_y y coordinate of the starting point of the first lane segment
     * @param p1_x x coordinate of the ending point of the first lane segment
     * @param p1_y y coordinate of the ending point of the first lane segment
     * @param p2_x x coordinate of the starting point of the second lane segment
     * @param p2_y y coordinate of the starting point of the second lane segment
     * @param p3_x x coordinate of the ending point of the second lane segment
     * @param p3_y y coordinate of the ending point of the second lane segment
     * @return null if no intersection point found or the point of intersection.
     */
    public static Point2f isColision(float p0_x, float p0_y, float p1_x, float p1_y,
                                     float p2_x, float p2_y, float p3_x, float p3_y) {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p1_x - p0_x;
        s1_y = p1_y - p0_y;
        s2_x = p3_x - p2_x;
        s2_y = p3_y - p2_y;

        float s, t;
        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            float i_x = p0_x + (t * s1_x);
            float i_y = p0_y + (t * s1_y);
            return new Point2f(i_x, i_y);
        }
        return null; // No collision
    }
}
