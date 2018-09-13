package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.agent;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.maneuver.*;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.RoadObject;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.VehicleSensor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.ManeuverAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class translates maneuvers generated by Agent to waypoints
 * Created by wmatex on 15.7.14.
 */
public class ManeuverTranslator {
    private static final int TRY_COUNT = 10;
    private double lastUpateTime;
    private static int RIGHT = -1;
    private static int LEFT = 1;
    private static int STRAIGHT = 0;

    private static final double RADIUS = 1f;
    private static final double MAX_ANGLE = Math.PI / 3;
    private static final float EPSILON = 0.01f;
    public static final float INNER_POINTS_STEP_SIZE = AgentDriveModel.adConfig.stepSize;
    private final static double MAX_SPEED = AgentDriveModel.adConfig.maximalSpeed;
    private static final float CIRCLE_AROUND = 3.0f;  // Does not exactly correspond to the actual wayPoint distance, used to make circle around the car


    private VehicleSensor sensor;
    private RouteNavigator navigator;
    private final int id;

    public ManeuverTranslator(int id, RouteNavigator navigator) {
        this.id = id;
        this.navigator = navigator;
    }

    public void setSensor(VehicleSensor sensor) {
        this.sensor = sensor;
    }


    public List<Action> prepare() {
        RoadObject me = sensor.senseCurrentState();
        if (me == null) {
            return translate(null);
        } else {
            findMyPosition(me);
            return generateWaypointInLane(me, STRAIGHT, null);
        }
    }

    /**
     * Method used for translating manoeuvre.
     *
     * @param maneuver translated manoeuvre.
     * @return list of actions.
     */
    public List<Action> translate(CarManeuver maneuver) {
        if (maneuver == null) {
            LinkedList<Action> actions = new LinkedList<Action>();
            if (navigator.isMyLifeEnds()) {
                actions.add(new WPAction(id, 0d, new Point3f(0, 0, 0), -1));
                return actions;
            }
            Point2f initial = navigator.getInitialPosition();
            Point2f next = navigator.nextWithReset();
            actions.add(new WPAction(id, 0d, new Point3f(initial.x, initial.y, 0), 0));
            actions.add(new WPAction(id, 0d, new Point3f(next.x, next.y, 0), 0));


            return actions;
        }

        RoadObject me = sensor.senseCurrentState();
        // Check the type of maneuver
        if ((maneuver instanceof StraightManeuver) || (maneuver instanceof AccelerationManeuver)
                || (maneuver instanceof DeaccelerationManeuver)) {
            return generateWaypointInLane(me, STRAIGHT, maneuver);
        } else if (maneuver instanceof LaneLeftManeuver) {
            return generateWaypointInLane(me, LEFT, maneuver);
        } else if (maneuver instanceof LaneRightManeuver) {
            return generateWaypointInLane(me, RIGHT, maneuver);
        } else {
            LinkedList<Action> actions = new LinkedList<Action>();
            ManeuverAction res = new ManeuverAction(sensor.getId(), maneuver.getStartTime() / 1000.0,
                    maneuver.getVelocityOut(), maneuver.getLaneOut(), maneuver.getDuration());
            actions.add(res);
            return actions;
        }
    }

    /**
     * This method is for translating manoeuvre into the waipoints plan
     *
     * @param relativeLane this determines if the manoeuvre is lane-changing.
     * @param maneuver     translated manoeuvre.
     * @return List of waypoint actions.
     */
    private List<Action> generateWaypointInLane(RoadObject me, int relativeLane, CarManeuver maneuver) {

        LinkedList<Action> actions = new LinkedList<Action>();
        List<Point2f> wps = new LinkedList<Point2f>();

        Point2f initial = navigator.getInitialPosition();
        Point2f current = new Point2f(me.getPosition().getX(), me.getPosition().getY());

        if (initial.equals(current))
            actions.add(new WPAction(id, 0d, new Point3f(initial.x, initial.y, 0), me.getVelocity().length()));
        ArrayList<Point3f> points;  // list of points on the way, used to be able to set speed to the action later

        int wpCount = (int) me.getVelocity().length() * 2 + 1; // how many waypoints before me will be calculated.
        points = new ArrayList<Point3f>();
        if (navigator.isMyLifeEnds()) {
            actions.add(new WPAction(id, 0d, new Point3f(0, 0, 0), -1));
            return actions;
        }
        navigator.setCheckpoint();

        if (relativeLane == RIGHT) {
            navigator.changeLaneRight();
            navigator.setCheckpoint();
        } else if (relativeLane == LEFT) {
            navigator.changeLaneLeft();
            navigator.setCheckpoint();
        }

        Point2f waypoint = navigator.getRoutePoint();

        double minSpeed = Float.MAX_VALUE; // minimal speed on the points before me
        for (int i = 0; (maneuver != null && i <= (maneuver.getPositionOut() / INNER_POINTS_STEP_SIZE) + 1) || i < wpCount; i++) {
            while (!navigator.isMyLifeEnds() && waypoint.distance(navigator.getRoutePoint()) < CIRCLE_AROUND) {
                navigator.advanceInRoute();
            }
            if (navigator.isMyLifeEnds()) {
                if (i == 0) {
                    actions.add(new WPAction(id, 0d, new Point3f(0, 0, 0), -1));
                    return actions;
                }
                wpCount = i;
                break;
            }
            waypoint = navigator.getRoutePoint();
            wps.add(waypoint);
            // vector from my position to the next waypoint
            Vector3f toNextPoint = new Vector3f(waypoint.x - me.getPosition().x, waypoint.y - me.getPosition().y, 0);
            Vector3f velocity = me.getVelocity();
            float angle = velocity.angle(toNextPoint); // angle between my velocity and vector to the next point
            double speed;
            if (Float.isNaN(angle)) {
                speed = 1;
            } else {
                if (angle < 0.4) speed = MAX_SPEED; // if the curve is less than 20 degrees, go by the max speed.
                else if (angle > 6) speed = 2;    // minimal speed for curves.
                else {
                    speed = 1 / angle * 6;
                }
            }
            if (speed < minSpeed)
                minSpeed = speed;  // all the next actions get the minimal speed.
            points.add(i, new Point3f(waypoint.x, waypoint.y, me.getPosition().z));
        }
        if (maneuver != null && minSpeed > maneuver.getVelocityOut()) {
            minSpeed = (float) maneuver.getVelocityOut();
        }
        double speedChangeConst = (me.getVelocity().length() - minSpeed) / wpCount;
        if (speedChangeConst < -0.6) speedChangeConst = -0.6;
        for (int i = 0; i < wpCount; i++) // actual filling my outgoing actions
        {
            //scaling speed to the lowest
            actions.add(new WPAction(sensor.getId(), me.getUpdateTime(), points.get(i), me.getVelocity().length() - (i + 1) * speedChangeConst));
        }
        navigator.resetToCheckpoint();
        lastUpateTime = me.getUpdateTime();
        return actions;

    }

    /**
     * This method computes the distance of the waypoint. It is the Euklidian distance of the waypoint
     * multiplied by absolute value of the sin of the angle between the direction of the waypoint
     * and the vector of velocity. This ensures, that waypoints that are less deviating from
     * the direction of the vehicle's movement and are close enough are picked.
     */
    public float distance(Point2f innerPoint, Point2f position, Vector2f direction, Vector2f velocity) {
        float d = innerPoint.distance(position);
        return d * d * Math.abs((float) Math.sin(direction.angle(velocity)) + EPSILON);
    }

    public void findMyPosition(RoadObject me) {
        Point2f position2D = new Point2f(me.getPosition().getX(), me.getPosition().getY());

        //try to advance navigator closer to the actual position
        int maxMove = TRY_COUNT;  // how many points will be tried.
        //how many waiponts ahead will be chcecked depending on the update time
        maxMove = (int) (((me.getUpdateTime() - lastUpateTime) * MAX_SPEED) / 1000) + 5;
        if (maxMove < TRY_COUNT) maxMove = TRY_COUNT;
        if (true) { //Simulator dependent code.
            //The difference is in switching between lanes or edges.
            String uniqueIndex = navigator.getUniqueLaneIndex();
            while (!navigator.isMyLifeEnds() && maxMove-- > 0 && navigator.getRoutePoint().distance(position2D) > CIRCLE_AROUND && navigator.getUniqueLaneIndex().equals(uniqueIndex)) {
                navigator.advanceInRoute();
            }
            if (!navigator.getUniqueLaneIndex().equals(uniqueIndex)) {
                float initialPos = position2D.distance(navigator.getRoutePoint());
                do {
                    navigator.advanceInRoute();
                } while (position2D.distance(navigator.getRoutePoint()) < initialPos);
                while (navigator.getRoutePoint().distance(position2D) <= CIRCLE_AROUND) {
                    navigator.advanceInRoute();
                }
                navigator.setCheckpoint();
            }
            if (navigator.getRoutePoint().distance(position2D) > CIRCLE_AROUND && navigator.getUniqueLaneIndex().equals(uniqueIndex)) {
                navigator.resetToCheckpoint();
            } else {
                while (!navigator.isMyLifeEnds() && navigator.getRoutePoint().distance(position2D) <= CIRCLE_AROUND) {
                    navigator.advanceInRoute();
                }
                //    navigator.setCheckpoint();
            }
        } else {
            while (!navigator.isMyLifeEnds() && maxMove-- > 0 && navigator.getRoutePoint().distance(position2D) > CIRCLE_AROUND) {
                navigator.advanceInRoute();
            }
            if (navigator.getRoutePoint().distance(position2D) > CIRCLE_AROUND) {
                navigator.resetToCheckpoint();
            } else {
                while (!navigator.isMyLifeEnds() && navigator.getRoutePoint().distance(position2D) <= CIRCLE_AROUND) {
                    navigator.advanceInRoute();
                }
                //    navigator.setCheckpoint();
            }
        }
    }

    public void setNavigator(RouteNavigator navigator) {
        this.navigator = navigator;
    }
}
