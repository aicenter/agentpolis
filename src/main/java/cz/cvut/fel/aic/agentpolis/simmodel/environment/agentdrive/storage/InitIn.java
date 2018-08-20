package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.storage;

import java.util.Collection;

import javax.vecmath.Point3d;

public class InitIn {
	
	private final Collection<Point3d> points;
	
	
	public InitIn(Collection<Point3d> points) {
		this.points = points;
	}
	
	public Collection<Point3d> getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return "InitIn [points=" + points + "]";
	}

}
