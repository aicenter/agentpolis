package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import javax.vecmath.Point2d;

public class CarIn {
	
	private final int id;
	private final double updateTime;
	private final Point2d position;
	private final Point2d highwayPoint;
	private final double v;
	private final int lane;
	
	public CarIn(int id, double t, Point2d position, Point2d highwayPoint, double v, int lane) {
		this.id = id;
		this.updateTime = t;
		this.position = position;
		this.highwayPoint = highwayPoint;
		this.v = v;
		this.lane = lane;
	}

	public int getId() {
		return id;
	}
	
	public double getUpdateTime() {
		return updateTime;
	}

	public Point2d getPosition() {
		return position;
	}
	
	public Point2d getHighwayPoint() {
		return highwayPoint;
	}
	
	public double getV() {
		return v;
	}

	public int getLane() {
		return lane;
	}

	@Override
	public String toString() {
		return "CarIn [id=" + id + ", pos=" + position + ", hp=" + highwayPoint + ", v=" + v + ", lane=" + lane + "]";
	}

}
