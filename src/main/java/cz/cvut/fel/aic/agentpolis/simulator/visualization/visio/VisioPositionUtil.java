/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point2d;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.MovingEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.ShapeUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.Node;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;

@Singleton
public class VisioPositionUtil {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(VisioPositionUtil.class);

	private final Map<Integer, ? extends Node> nodesFromAllGraphs;

	private final Graph<SimulationNode, SimulationEdge> highwayNetwork;

	private final Graph<SimulationNode, SimulationEdge> pedestrianNetwork;

	private final TimeProvider timeProvider;

	private final ShapeUtils shapeUtils;

	private final Map<MovingEntity, Double> movingAgentAngle = new HashMap<>();

	@Inject
	public VisioPositionUtil(AllNetworkNodes allNetworkNodes, HighwayNetwork highwayNetwork,
			PedestrianNetwork pedestrianNetwork, TimeProvider timeProvider, ShapeUtils shapeUtils) {
		this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
		this.highwayNetwork = highwayNetwork.getNetwork();
		this.pedestrianNetwork = pedestrianNetwork.getNetwork();
		this.timeProvider = timeProvider;
		this.shapeUtils = shapeUtils;
	}

	public Node getNode(int nodeId) {
		return nodesFromAllGraphs.get(nodeId);
	}

	public SimulationEdge getEdge(int fromNodeId, int toNodeId, EGraphType type) {
		switch (type) {
			case HIGHWAY :
				return highwayNetwork.getEdge(highwayNetwork.getNode(fromNodeId), highwayNetwork.getNode(toNodeId));
			case PEDESTRIAN :
				return pedestrianNetwork.getEdge(pedestrianNetwork.getNode(fromNodeId),
						pedestrianNetwork.getNode(toNodeId));
			default :
				throw new IllegalArgumentException();
		}
	}

	public Point2d getCanvasPosition(GPSLocation position) {
		GPSLocation WGS84position = getWebMercatorPosition(position);
		return new Point2d(Vis.transX(WGS84position.getLongitudeProjected()),
				Vis.transY(WGS84position.getLatitudeProjected()));
	}

	public Point2d getCanvasPosition(int nodeId) {
		return getCanvasPosition(getNode(nodeId));
	}

	public Point2d getCanvasPosition(AgentPolisEntity entity) {
		return getCanvasPosition(entity.getPosition());
	}

	public Point2d getCanvasPosition(Point2d position) {
		return new Point2d(Vis.transX(position.x), Vis.transY(position.y));
	}

	public static GPSLocation getWebMercatorPosition(GPSLocation position) {
		return GPSLocationTools.createGPSLocation(position.getLatitude(), position.getLongitude(), 0, 3857); // 3857 -
																												// Web
																												// Mercator
																												// projection
	}

	public static GPSLocation getWGS84PositionFromCustomProjection(int projectedlatitude1E2, int projectedlongitude1E2,
			int customprojectionSRID) {
		GPSLocation customProjection = GPSLocationTools.createGPSLocationFromProjected(projectedlatitude1E2,
				projectedlongitude1E2, 0, customprojectionSRID);
		GPSLocation webMercator = GPSLocationTools.createGPSLocation(customProjection.getLatitude(),
				customProjection.getLongitude(), 0, 3857); // 3857 - Web Mercator
		return webMercator;
	}

	private int getEdgeLength(int entityPositionNodeId, int targetNodeId) {
		return getEdge(entityPositionNodeId, targetNodeId, EGraphType.HIGHWAY).getLengthCm();
	}

	public int getTripLengthInMeters(Trip<SimulationNode> graphTrip) {
		return getTripLengthInMeters(graphTrip, null);
	}

	public int getTripLengthInMeters(Trip<SimulationNode> graphTrip, Node stopPosition) {
		int length = 0;

		SimulationNode[] locations = graphTrip.getAllLocations();

		int startNodeId = locations[0].getId();
		for (int i = 1; i < locations.length; i++) {
			int targetNodeId = locations[i].getId();
			length += getEdgeLength(startNodeId, targetNodeId);

			if (stopPosition != null && stopPosition.id == targetNodeId) {
				break;
			}

			startNodeId = targetNodeId;
		}

		return length;
	}

	private Point2d getCanvasPositionInterpolatedForTransportable(TransportableEntity entity) {
		TransportEntity transportEntity = entity.getTransportingEntity();
		if (transportEntity == null) {
			return getCanvasPosition((AgentPolisEntity) entity);
		} else {
			if (transportEntity instanceof Vehicle) {
				return getCanvasPositionInterpolatedForVehicle((Vehicle) transportEntity);
			} else {
				return getCanvasPositionInterpolated(transportEntity, EGraphType.HIGHWAY);
			}
		}
	}

	public Point2d getCanvasPositionInterpolatedForVehicle(Vehicle vehicle) {
		return getCanvasPositionInterpolated(vehicle, EGraphType.HIGHWAY);
	}

	public Point2d getCanvasPositionInterpolatedForVehicleInTime(Vehicle vehicle, long time) {
		return getCanvasPositionInterpolatedInTime(vehicle, EGraphType.HIGHWAY, time);
	}

	public Point2d getCanvasPositionInterpolatedInTime(MovingEntity entity, EGraphType type, long time) {
		// if the entity is transported
		if (entity instanceof TransportableEntity && ((TransportableEntity) entity).getTransportingEntity() != null) {
			return getCanvasPositionInterpolatedForTransportable((TransportableEntity) entity);
		}

		Node startNode = entity.getPosition();
		Node targetNode = entity.getTargetNode();

		// entity waits
		if (targetNode == null || targetNode == startNode) {
			return getCanvasPosition(startNode);
		}

		SimulationEdge edge = getEdge(startNode.getId(), targetNode.getId(), type);
		if (edge == null) {
			LOGGER.error("Invalid edge: from: {0}, to: {1}", startNode.getId(), targetNode.getId());
		}
		return getCanvasPositionInterpolatedInTime(edge, entity, time);
	}
	public Point2d getCanvasPositionInterpolated(MovingEntity entity, EGraphType type) {
		return getCanvasPositionInterpolatedInTime(entity, type, timeProvider.getCurrentSimTime());
	}

	public GPSLocation getPositionInterpolated(SimulationEdge edge, MovingEntity agent) {
		return getPositionInterpolatedInTime(edge, agent, timeProvider.getCurrentSimTime());
	}

	public GPSLocation getPositionInterpolatedInTime(SimulationEdge edge, MovingEntity agent, long time) {
		DelayData delayData = agent.getDelayData();
		
		double portionOfEdgeDistance;
		
		// delay data is set to null when the move action is finished
		if(delayData == null){
			portionOfEdgeDistance = 1;
		}
		else{
			double portionCompleted = (double) (time - delayData.getDelayStartTime()) / delayData.getDelay();
			if (portionCompleted > 1)
				portionCompleted = 1;
			double distanceOfDrivenInterval = delayData.getDelayDistance();
			portionOfEdgeDistance = (delayData.getStartDistanceOffset()
					+ distanceOfDrivenInterval * portionCompleted) / edge.getLengthCm();
			portionOfEdgeDistance = Math.min(Math.max(0.0, portionOfEdgeDistance), 1.0);
		}

		ShapeUtils.PositionAndAngle positionAndAngleOnPath = shapeUtils.getPositionAndAngleOnPath(edge.shape,
				portionOfEdgeDistance);
		movingAgentAngle.put(agent, positionAndAngleOnPath.angle);
		return positionAndAngleOnPath.point;
	}

	public Point2d getCanvasPositionInterpolated(SimulationEdge edge, MovingEntity agent) {
		return getCanvasPosition(getPositionInterpolated(edge, agent));
	}

	public Point2d getCanvasPositionInterpolatedInTime(SimulationEdge edge, MovingEntity agent, long time) {
		return getCanvasPosition(getPositionInterpolatedInTime(edge, agent, time));
	}

	public double getAngle(MovingEntity agent) {
		return movingAgentAngle.getOrDefault(agent, 0d);
	}

	public Point2d getCanvasPositionInterpolated(MovingEntity entity) {
		return getCanvasPositionInterpolated(entity, EGraphType.HIGHWAY);
	}

	public Point2d getCanvasPositionInterpolatedInTime(MovingEntity entity, long time) {
		return getCanvasPositionInterpolatedInTime(entity, EGraphType.HIGHWAY, time);
	}
}
