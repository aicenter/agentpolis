package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage.vehicle;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Class representing a car
 * <p/>
 * Created by wmatex on 3.7.14.
 */
public class Car extends Vehicle {
    public Car(int id, int lane, Point3f position, Vector3f heading, float velocity) {
        super(id, lane, position, heading, velocity);
    }

    /**
     * Update the car's position based on it's speed and previous position
     *
     * @param deltaTimeMs Time since last update
     */
    @Override
    public void update(long deltaTimeMs) {
        //update direction
        countSteeringAngle();

        float x = getPosition().getX();
        float y = getPosition().getY();
        float z = getPosition().getZ();

        /// Compute the new velocity vector based on these equations: http://planning.cs.uiuc.edu/node658.html
        double angleSpeed = getVelocity() / getAxeLength() * Math.tan(getSteeringAngle());
        double xAngle = Math.atan2(getHeading().getY(), getHeading().getX()) + angleSpeed * deltaTimeMs / 1000f;
        double velocitySize = getVelocity();
        Vector3f velocity = new Vector3f((float) (velocitySize * Math.cos(xAngle)), (float) (velocitySize * Math.sin(xAngle)),
                getHeading().getZ() * getVelocity());
        if (Float.isNaN(velocity.x) || Float.isNaN(velocity.y) || Float.isNaN(velocity.z)) {
            velocity = new Vector3f(0f, 0f, 0f);
        }

        x = x + velocity.getX() * deltaTimeMs / 1000f;
        y = y + velocity.getY() * deltaTimeMs / 1000f;
        z = z + velocity.getZ() * deltaTimeMs / 1000f;


        setVelocity(velocity.length());
        velocity.normalize();
        if (!(Float.isNaN(velocity.x) || Float.isNaN(velocity.y) || Float.isNaN(velocity.z))) {
            setHeading(velocity);
        }

        setPosition(new Point3f(x, y, z));
    }

    private void updateLane() {
//        Network network = null;
//        Point2f position2D = new Point2f(getPosition().x, getPosition().y);
//        Lane lane = network.getLane(position2D);
//        setLane(lane.getIndex());
        setLane(-1);
    }
}
