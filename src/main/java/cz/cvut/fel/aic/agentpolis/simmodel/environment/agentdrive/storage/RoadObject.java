package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class RoadObject {

    private int id = -1;
    private double updateTime = -1;
    private Point3f position;
    private Vector3f velocity;
    private int lane = -1;

    public RoadObject(int id, double updateTime, int laneIndex, Point3f position, Vector3f velocity) {
        this.id = id;
        this.updateTime = updateTime;
        this.lane = laneIndex;
        this.position = position;
        this.velocity = velocity;

    }

    public int getId() {
        return id;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public int getLaneIndex() {
        return lane;
    }

    public Point3f getPosition() {
        return position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setPosition(Point3f position) {
        this.position = position;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    @Override
    public String toString() {
        return "RoadObject [id = " + id + ", updateTime=" + updateTime + ", lane=" + lane
                + ", pos=" + position + ", v=" + velocity + "]";
    }
}
