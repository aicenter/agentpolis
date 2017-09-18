/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;
import cz.cvut.fel.aic.geographtools.Node;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;

import javax.vecmath.Point2d;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fido
 */
@Singleton
public class PositionUtil {

    public enum NetworkType {
        HIGHWAY,
        PEDESTRIAN
    }

    private final Map<Integer, ? extends Node> nodesFromAllGraphs;

    private final Graph<SimulationNode, SimulationEdge> highwayNetwork;

    private final Graph<SimulationNode, SimulationEdge> pedestrianNetwork;

    private final TimeProvider timeProvider;

    private final GraphSpec2D mapSpecification;


    @Inject
    public PositionUtil(AllNetworkNodes allNetworkNodes,
                        HighwayNetwork highwayNetwork,
                        PedestrianNetwork pedestrianNetwork,
                        TimeProvider timeProvider, GraphSpec2D mapSpecification) {
        this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
        this.highwayNetwork = highwayNetwork.getNetwork();
        this.pedestrianNetwork = pedestrianNetwork.getNetwork();
        this.timeProvider = timeProvider;
        this.mapSpecification = mapSpecification;
    }


    public Node getNode(int nodeId) {
        return nodesFromAllGraphs.get(nodeId);
    }

    public SimulationEdge getEdge(int fromNodeId, int toNodeId, NetworkType type) {
        switch (type) {
            case HIGHWAY:
                return highwayNetwork.getEdge(fromNodeId, toNodeId);
            case PEDESTRIAN:
                return pedestrianNetwork.getEdge(fromNodeId,toNodeId);
            default:
                throw new IllegalArgumentException();
        }
    }

    public Point2d getPosition(GPSLocation position) {
        return new Point2d(position.getLongitudeProjected(), position.getLatitudeProjected());
    }

    public Point2d getCanvasPosition(GPSLocation position) {
        return new Point2d(Vis.transX(position.getLongitudeProjected()), Vis.transY(position.getLatitudeProjected()));
    }

    public Point2d getCanvasPosition(int nodeId) {
        return getCanvasPosition(nodesFromAllGraphs.get(nodeId));
    }

    public Point2d getCanvasPosition(AgentPolisEntity entity) {
        return getCanvasPosition(entity.getPosition());
    }

    public Point2d getCanvasPosition(Point2d position) {
        return new Point2d(Vis.transX(position.x), Vis.transY(position.y));
    }

    public int getWorldWidth() {
//        Point2d minMin = getPosition(GPSLocationTools.createGPSLocation(mapBounds.getMinNode().getLatitude(), 
//                mapBounds.getMinNode().getLongitude(), mapBounds.getMinNode().elevation, transformer));
//        Point2d minMax = getPosition(GPSLocationTools.createGPSLocation(mapBounds.getMinNode().getLatitude(), 
//                mapBounds.getMaxNode().getLongitude(), mapBounds.getMaxNode().elevation, transformer));
//        Point2d minMin = getPosition(new GPSLocation(mapBounds.getMinNode(), mapBounds.getMinLonE6(), 0, 0));
//        Point2d minMin = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMinLonE6(), 0, 0));
//        Point2d minMax = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMaxLonE6(), 0, 0));
//
//        return (int) (minMax.x - minMin.x);
        return mapSpecification.getWidth();
    }

    public int getWorldHeight() {
//        Point2d minMin = getPosition(GPSLocationTools.createGPSLocation(mapBounds.getMinNode().getLatitude(), 
//                mapBounds.getMinNode().getLongitude(), mapBounds.getMinNode().elevation, transformer));
//        Point2d maxMin = getPosition(GPSLocationTools.createGPSLocation(mapBounds.getMaxNode().getLatitude(), 
//                mapBounds.getMinNode().getLongitude(), mapBounds.getMaxNode().elevation, transformer));
//        Point2d minMin = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMinLonE6(), 0, 0));
//        Point2d maxMin = getPosition(new GPSLocation(mapBounds.getMaxLatE6(), mapBounds.getMinLonE6(), 0, 0));
//
//        return (int) (minMin.y - maxMin.y);
        return mapSpecification.getHeight();
    }

    private int getEdgeLength(int entityPositionNodeId, int targetNodeId) {
        return highwayNetwork.getEdge(entityPositionNodeId, targetNodeId).getLength();
    }


    public int getTripLengthInMeters(GraphTrip<TripItem> graphTrip) {
        int length = 0;

        List<TripItem> locations = graphTrip.getLocations();

        int stratNodeId = locations.get(0).tripPositionByNodeId;
        for (int i = 1; i < locations.size(); i++) {
            int targetNodeId = locations.get(i).tripPositionByNodeId;
            length += getEdgeLength(stratNodeId, targetNodeId);
            stratNodeId = targetNodeId;
        }

        return length;
    }

    private Point2d getCanvasPositionInterpolatedForTransportable(TransportableEntity entity) {
        TransportEntity transportEntity = entity.getTransportingEntity();
        if (transportEntity == null) {
            return getCanvasPosition((AgentPolisEntity) entity);
        } else {
            if (transportEntity instanceof MovingAgent) {
                return getCanvasPositionInterpolated((MovingAgent) transportEntity, NetworkType.HIGHWAY);
            } else {
                return getCanvasPositionInterpolatedForVehicle((Vehicle) transportEntity);
            }
        }
    }

    public Point2d getCanvasPositionInterpolatedForVehicle(Vehicle vehicle) {

        // if the vehicle itself is being transported
        if (vehicle instanceof TransportableEntity && ((TransportableEntity) vehicle).getTransportingEntity() != null) {
            return getCanvasPositionInterpolatedForTransportable((TransportableEntity) vehicle);
        }

        Driver driver = vehicle.getDriver();
        if (driver == null) {
            return getCanvasPosition(vehicle);
        }

        Node currentNode = vehicle.getDriver().getPosition();
        Node targetNode = vehicle.getDriver().getTargetNode();

        /* driver is in the car but he does not drive */
        if (targetNode == null) return getCanvasPosition(vehicle);

        // edge length
        SimulationEdge edge = getEdge(currentNode.id, targetNode.id, NetworkType.HIGHWAY);
        double length = getEdgeLength(edge);

        // portion of the edge that is free for ride
        double portion = 1d - (vehicle.getQueueBeforeVehicleLength() / length);

        return getCanvasPositionInterpolated(edge, portion, vehicle.getDriver());

    }

    public Point2d getCanvasPositionInterpolated(MovingAgent entity, NetworkType type) {

        // if the entity is transported
        if (entity instanceof TransportableEntity && ((TransportableEntity) entity).getTransportingEntity() != null) {
            return getCanvasPositionInterpolatedForTransportable((TransportableEntity) entity);
        }

        Node startNode = entity.getPosition();
        Node targetNode = entity.getTargetNode();

        // entity waits 
        if (targetNode == null) {
            return getCanvasPosition(startNode);
        }

        SimulationEdge edge = getEdge(startNode.id, targetNode.id, type);
        if (edge == null) {
            //throw new NullPointerException();
            return new Point2d(0,0);
        }
        return getCanvasPositionInterpolated(edge, 1, entity);
    }

    private Point2d getCanvasPositionInterpolated(SimulationEdge edge, double portion, MovingAgent agent) {
        double portionCompleted = (double) (timeProvider.getCurrentSimTime() - agent.getDelayData().getDelayStartTime())
                / agent.getDelayData().getDelay();
        if (portionCompleted > 1) portionCompleted = 1;
        double lengthCompleted = portionCompleted * getEdgeLength(edge) * portion;
        EdgeShape shape = edge.shape;
        int i = 0;
        double edgePartLength = GPSLocationTools.computeDistance(shape.get(0), shape.get(1));
        while (edgePartLength < lengthCompleted && i < shape.size() - 2) {
            lengthCompleted -= edgePartLength;
            i++;
            edgePartLength = GPSLocationTools.computeDistance(shape.get(i), shape.get(i + 1));
        }

        if (edge.shape.getSegmentAngles() == null) applySegmentAngles(edge.shape);
        movingAgentAngle.put(agent, edge.shape.getSegmentAngles()[i]);

        Point2d startPosition = getCanvasPosition(shape.get(i));
        Point2d targetPosition = getCanvasPosition(shape.get(i + 1));

        double xIncrement = (targetPosition.x - startPosition.x) * lengthCompleted / edgePartLength;
        double yIncrement = (targetPosition.y - startPosition.y) * lengthCompleted / edgePartLength;

        return new Point2d(startPosition.x + xIncrement, startPosition.y + yIncrement);
    }

    private void applySegmentAngles(EdgeShape shape) {
        double[] angles = new double[shape.size() - 1];
        for (int i = 0; i < angles.length; i++) {
            angles[i] = getAngle(shape.get(i), shape.get(i + 1));
        }
        shape.setSegmentAngles(angles);
    }

    public GPSLocation getPointOnEdge(SimulationEdge edge, double portion) {
        double lengthCompleted = getEdgeLength(edge) * portion;
        EdgeShape shape = edge.shape;
        int i = 0;
        double edgePartLength = GPSLocationTools.computeDistance(shape.get(0), shape.get(1));
        while (edgePartLength < lengthCompleted) {
            lengthCompleted -= edgePartLength;
            i++;
            edgePartLength = GPSLocationTools.computeDistance(shape.get(i), shape.get(i + 1));
        }
        GPSLocation startPosition = shape.get(i);
        GPSLocation targetPosition = shape.get(i + 1);
        return getPointOnVector(startPosition, targetPosition, portion);
    }

    private GPSLocation getPointOnVector(GPSLocation gps1, GPSLocation gps2, double portion) {
        int xIncrement = (int) Math.round((gps2.getLongitudeProjected1E2()
                - gps1.getLongitudeProjected1E2()) * portion);
        int yIncrement = (int) Math.round((gps2.getLatitudeProjected1E2()
                - gps1.getLatitudeProjected1E2()) * portion);

        return new GPSLocation(0, 0, gps1.getLatitudeProjected1E2() + yIncrement,
                gps1.getLongitudeProjected1E2() + xIncrement);
    }

    private double getEdgeLength(SimulationEdge e) {
        double length = e.shape.getVisualLength();
        if (length != 0)
            return length;

        EdgeShape shape = e.shape;
        for (int i = 1; i < shape.size(); i++)
            length += getDistance(shape.get(i - 1), shape.get(i));
        e.shape.setVisualLength(length);
        return length;
    }

    public double getDistance(GPSLocation gps1, GPSLocation gps2) {
        return Math.sqrt(Math.pow(Math.abs(gps1.getLatitudeProjected() - gps2.getLatitudeProjected()), 2)
                + Math.pow(Math.abs(gps1.getLongitudeProjected() - gps2.getLongitudeProjected()), 2));
    }

    private HashMap<MovingAgent, Double> movingAgentAngle = new HashMap<>();

    public double getAngle(MovingAgent agent) {
        if (!movingAgentAngle.containsKey(agent))
            return 0;
        return movingAgentAngle.get(agent);
    }

    private double getAngle(GPSLocation position, GPSLocation target) {
        double dy = target.getLatitudeProjected1E2() - position.getLatitudeProjected1E2();
        double dx = target.getLongitudeProjected1E2() - position.getLongitudeProjected1E2();
        return Math.atan2(dy, dx);
    }
}
