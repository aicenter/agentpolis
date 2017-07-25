package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;

import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.GPSLocation;

import cz.agents.alite.vis.Vis;

/**
 * This class computes projection of point in latitude and longitude to point on screen. Computation based on {@link
 * BoundingBox} of the area and window size.
 *
 * @author Libor Wagner
 * @author Zbynek Moler
 */
public class Projection {

	/**
	 * Bounds of displayed area.
	 */
	private final BoundingBox bounds;

	/**
	 * Height of {@link Vis} window.
	 */
	public final int sceneHeight;

	/**
	 * Width of {@link Vis} window.
	 */
	public final int sceneWidth;

	/**
	 * Cache of computed values.
	 */
	private final Map<GPSLocation, Point3d> cache = new HashMap<>();

	private Projection(BoundingBox bounds, int sceneHeight, int sceneWidth) {
		super();
		this.bounds = bounds;
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
	}

	/**
	 * Project {@link GPSLocation} to point on screen and cache this value. This is a very simple projection, use only
	 * for visualization.
	 *
	 * @param location
	 * 		Latitude and longitude to be projected.
	 *
	 * @return Projected point.
	 */

	public Point3d project(GPSLocation location) {
		Point3d point = cache.get(location);

		if (point == null) {

			// Compute projection
			double bMinLon = bounds.getMinLonE6();
			double bMaxLon = bounds.getMaxLonE6();
			double bMinLat = bounds.getMinLatE6();
			double bMaxLat = bounds.getMaxLatE6();

			double x = (location.getLonE6() - bMinLon) / (bMaxLon - bMinLon) * sceneWidth;
			double y = sceneHeight - (location.getLatE6() - bMinLat) / (bMaxLat - bMinLat) * sceneHeight;

			// Create new point
			point = new Point3d(x, y, 0);

			// Cache new point
			cache.put(location, point);
		}

		return point;
	}

	public static Projection createGPSTo3DProjector(BoundingBox bounds, int sceneHeight, int sceneWidth) {
		return new Projection(bounds, sceneHeight, sceneWidth);

	}

}
