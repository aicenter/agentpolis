package cz.cvut.fel.aic.agentpolis.simulator.visualization.googleearth.entity.movement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import cz.agents.agentpolis.apgooglearth.regionbounds.RegionBounds;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.entityvelocitymodel.EntityVelocityModel;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.geographtools.Node;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

/**
 * The class interpolates the vehicle movement
 *
 * @author Zbynek Moler
 */
public class EntityMovementGE {

	private static final double EARTHS_RADIUS_IN_METERS = 6371000;

	private final Map<String, LinkedList<Coordinate>> entityDestinationCoordinateCache = new HashMap<>();
	private final Map<String, PreviousEntityMovement> previousEntityMovementCache = new HashMap<>();

	private final Map<Integer, ? extends Node> nodesFromAllGraphs;

	private final EventProcessor eventProcessor;
	private final EntityVelocityModel maxEntitySpeedStorage;

	public EntityMovementGE(Map<Integer, ? extends Node> nodesFromAllGraphs, EventProcessor eventProcessor,
							EntityVelocityModel maxEntitySpeedStorage) {
		super();
		this.nodesFromAllGraphs = nodesFromAllGraphs;
		this.eventProcessor = eventProcessor;
		this.maxEntitySpeedStorage = maxEntitySpeedStorage;
	}

	public synchronized Map<String, Coordinate> getEntityPosition(Set<String> entityIds, RegionBounds activeRegionBounds) {

		Map<String, Coordinate> entityMovement = new HashMap<>();

		for (String entityId : entityIds) {

//			Integer currentEntityPositionById = entityPositionStorage.getEntityPositionByNodeId(entityId);
            Integer currentEntityPositionById = 0;

			if (currentEntityPositionById == null) {
				continue;
			}

			Node currentEntityPosition = nodesFromAllGraphs.get(currentEntityPositionById);

			double lon = currentEntityPosition.getLongitude();
			double lat = currentEntityPosition.getLatitude();

			if (!activeRegionBounds.contains(lon, lat)) {
				continue;
			}

			Coordinate newCoordinate = new Coordinate(lon, lat);

			LinkedList<Coordinate> entityDestinationCoordinates = getCacheCoordinate(entityId);

			if (!newCoordinate.equals(entityDestinationCoordinates.peekLast())) {
				entityDestinationCoordinates.add(newCoordinate);
			}

			PreviousEntityMovement previousEntityMovement = getOrInitPreviousEntityMovement(entityId, newCoordinate);

			Coordinate firstFormDestination = entityDestinationCoordinates.peekFirst();
			Coordinate previousEntityMovementCoordinate = previousEntityMovement.getCoordinate();

			double newDistanceForMove = computeDistanceBaseOnEntitySpeedAndSimulationTime(entityId,
					previousEntityMovement
					.getLastSimulationTime());

			Coordinate nextMoveCoordinate = getNextMoveCoordinate(newDistanceForMove, firstFormDestination,
					previousEntityMovementCoordinate, entityDestinationCoordinates);

			previousEntityMovementCache.put(entityId, new PreviousEntityMovement(nextMoveCoordinate, eventProcessor
					.getCurrentTime()));
			entityDestinationCoordinateCache.put(entityId, entityDestinationCoordinates);
			entityMovement.put(entityId, nextMoveCoordinate);

		}
		return entityMovement;
	}

	public synchronized void clearMovementForEntity(String entityId) {
		entityDestinationCoordinateCache.remove(entityId);
		previousEntityMovementCache.remove(entityId);

	}

	private Coordinate getNextMoveCoordinate(double newDistanceForMove, Coordinate firstFormDestination,
											 Coordinate previousEntityMovementCoordinate,
											 LinkedList<Coordinate> entityDesctinationCoordinates) {

		double latLast = previousEntityMovementCoordinate.getLatitude();
		double latNew = firstFormDestination.getLatitude();

		double lonLast = previousEntityMovementCoordinate.getLongitude();
		double lonNew = firstFormDestination.getLongitude();


		double distBetweenInMeters = distanceInMeters(latLast, lonLast, latNew, lonNew);

		Coordinate nextMoveCoordinate;

		if (distBetweenInMeters < newDistanceForMove) {
			nextMoveCoordinate = firstFormDestination;
			entityDesctinationCoordinates.removeFirst();
		} else {
			double courseBetweenNewAndLastCoordinateInRadians = course(latLast, lonLast, latNew, lonNew);
			nextMoveCoordinate = computeNewLatFromStartPointAndDistance(latLast, lonLast, newDistanceForMove,
					courseBetweenNewAndLastCoordinateInRadians);
		}

		return nextMoveCoordinate;
	}

	private PreviousEntityMovement getOrInitPreviousEntityMovement(String entityId, Coordinate newCoordinate) {
		PreviousEntityMovement previousEntityMovement = previousEntityMovementCache.get(entityId);
		if (previousEntityMovement == null) {
			previousEntityMovement = new PreviousEntityMovement(newCoordinate, eventProcessor.getCurrentTime());
		}
		return previousEntityMovement;
	}

	private double computeDistanceBaseOnEntitySpeedAndSimulationTime(String entityId, long lastSimulationTime) {

		double entityMaxSpeed = maxEntitySpeedStorage.getEntityVelocityInmps(entityId);
		double time = (eventProcessor.getCurrentTime() - lastSimulationTime) / 1000; // to
		// second
		return entityMaxSpeed * time;
	}

	/**
	 * Destination point given distance and bearing from start point
	 * <p>
	 * see http://www.movable-type.co.uk/scripts/latlong.html
	 *
	 * @param latOfStartPointInDegrees
	 * @param lonOfStartPointInDegrees
	 * @param distanceToComputedPointInMeters
	 * @param bearingCourseItRadians
	 *
	 * @return
	 */
	private Coordinate computeNewLatFromStartPointAndDistance(double latOfStartPointInDegrees,
															  double lonOfStartPointInDegrees,
															  double distanceToComputedPointInMeters,
															  double bearingCourseItRadians) {

		double latOfStartPointInRadians = Math.toRadians(latOfStartPointInDegrees);
		double lonOfStartPointInRadians = Math.toRadians(lonOfStartPointInDegrees);

		double sinOfLatOfStartPointInRadians = Math.sin(latOfStartPointInRadians);
		double cosOfLatOfStartPointInRadians = Math.cos(latOfStartPointInRadians);
		double angularDistnaceInRadians = distanceToComputedPointInMeters / EARTHS_RADIUS_IN_METERS;
		double sinOfangularDistnaceInRadians = Math.sin(angularDistnaceInRadians);
		double cosOfangularDistnaceInRadians = Math.cos(angularDistnaceInRadians);

		double latComputed = Math.asin(sinOfLatOfStartPointInRadians * cosOfangularDistnaceInRadians +
									   cosOfLatOfStartPointInRadians * sinOfangularDistnaceInRadians *
									   Math.cos(bearingCourseItRadians));

		double lonComputed = lonOfStartPointInRadians + Math.atan2(Math.sin(bearingCourseItRadians) *
																   sinOfangularDistnaceInRadians *
																   cosOfLatOfStartPointInRadians,
				cosOfangularDistnaceInRadians - sinOfLatOfStartPointInRadians * Math.sin(latComputed));

		return new Coordinate(Math.toDegrees(lonComputed), Math.toDegrees(latComputed));

	}

	private LinkedList<Coordinate> getCacheCoordinate(String entityId) {
		LinkedList<Coordinate> cacheCoordinates = entityDestinationCoordinateCache.get(entityId);
		if (cacheCoordinates == null) {
			cacheCoordinates = new LinkedList<>();
		}
		return cacheCoordinates;
	}

	//implementation taken from libosm-atg
	public static double course(double lat1, double lon1, double lat2, double lon2) {
		return Math.atan2(
				Math.sin(lon1 - lon2) * Math.cos(lat2),
				Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2)) %
			   6.283185307179586D;
	}

	//implementation taken from libosm-atg
	public static double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
		if(lat1 == lat2 && lon1 == lon2) {
			return 0.0D;
		} else {
			boolean constantTerm = true;
			boolean factor1 = true;
			double angle = Math.toRadians((lat1 + lat2) / 2.0D);
			int factor = (int)((6378.0D - 21.0D * Math.sin(angle)) * 1000.0D);
			double p1lat = Math.toRadians(lat1);
			double p2lat = Math.toRadians(lat2);
			return Math.acos(Math.sin(p1lat) * Math.sin(p2lat) + Math.cos(p1lat) * Math.cos(p2lat) * Math.cos(Math.toRadians(lon2 - lon1))) * (double)factor;
		}
	}

	private class PreviousEntityMovement {

		private final Coordinate coordinate;
		private final long lastSimulationTime;

		public PreviousEntityMovement(Coordinate coordinate, long lastSimulationTime) {
			super();
			this.coordinate = coordinate;
			this.lastSimulationTime = lastSimulationTime;
		}

		public Coordinate getCoordinate() {
			return coordinate;
		}

		public long getLastSimulationTime() {
			return lastSimulationTime;
		}

	}

}
