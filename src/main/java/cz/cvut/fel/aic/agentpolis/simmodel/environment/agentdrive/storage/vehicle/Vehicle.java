package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.vehicle;



import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller.PlanController;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.controller.VelocityController;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.Action;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.plan.WPAction;

import java.util.Collection;
import java.util.LinkedList;

import javax.vecmath.*;

/**
 * Base class for vehicles handled by the simulator.
 * The class defines some basic properties of the vehicle like position and speed.
 *
 * Created by wmatex on 3.7.14.
 */
public abstract class Vehicle {

    private static final float MAX_STEERING = (float)Math.PI/3;

    private int id;
    private int lane;
    private Point3f position;

    /// Magnitude of the velocity vector. The velocity vector can be obtained as: velocity * heading
    private float velocity;

    /// Heading of the vehicle
    private Vector3f heading;

    private PlanController planController;

    /// Length between front and rear axe
    private float axeLength = 3f;

    /// Angle of the steering wheels
    private float steeringAngle = 0f;

    /// Controller of vehicle magnitude
    private final VelocityController velocityController;

    private final Point3f initialPosition;
    private final float initialVelocity;

    public Point3f getInitialPosition() {
        return initialPosition;
    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    protected Vehicle(int id, int lane, Point3f position, Vector3f heading, float velocity) {
        this.id = id;
        this.lane = lane;
        this.position = position;
        this.heading = heading;
        this.velocity = velocity;
        this.initialPosition = position;
        this.initialVelocity = velocity;

        planController = new PlanController();
        velocityController = new VelocityController(this);
    }

    public int getId() {
        return id;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public Point3f getPosition() {
        return position;
    }

    public void setPosition(Point3f position) {
        this.position = position;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void scaleVelocity(float factor) {
        velocity *= factor;
    }

    public void setVelocityVector(Vector3f velocity) {
        setVelocity(velocity.length());
        velocity.normalize();
        setHeading(velocity);
    }

    public Vector3f getHeading() {
        return heading;
    }

    protected void setHeading(Vector3f heading) {
        this.heading = heading;
    }

    public float getAxeLength() {
        return axeLength;
    }

    public float getSteeringAngle() {
        return steeringAngle;
    }

    public void setSteeringAngle(float steeringAngle) {
        if (steeringAngle < -MAX_STEERING) {
            this.steeringAngle =  -MAX_STEERING;
        } else if (steeringAngle > MAX_STEERING) {
            this.steeringAngle = MAX_STEERING;
        } else {
            this.steeringAngle = steeringAngle;
        }
    }
    
    public PlanController getPlanController() {
        return planController;
    }

    public VelocityController getVelocityController() {
        return velocityController;
    }

    public void countSteeringAngle(){
        Point3f targetPos = planController.getNextWayPoint(position,velocity);
        if (targetPos != null) {
            Vector3f direction = new Vector3f(
                    targetPos.getX() - position.getX(),
                    targetPos.getY() - position.getY(),
                    targetPos.getZ() - position.getZ());
            float actSteeringAngle = (float) Math.atan2(
                    heading.getX()*direction.getY() - heading.getY()*direction.getX(),
                    heading.getX()*direction.getX() + heading.getY()*direction.getY() );

            setSteeringAngle(actSteeringAngle);
        }
    }

    public void setWayPoints(Collection<Action> Actions){
        LinkedList<Point3f> wayPoints = new LinkedList<>();
        for (Action action : Actions){
            if (action.getClass().equals(WPAction.class)) {
                WPAction wpAction = (WPAction) action;
                wayPoints.add(wpAction.getPosition());
            }
        }
        planController.setWayPoints(wayPoints);
    }

    /**
     * Update the vehicle position
     *
     * @param deltaTime Time elapsed since last update in milliseconds
     */
    public abstract void update(long deltaTime);
}

