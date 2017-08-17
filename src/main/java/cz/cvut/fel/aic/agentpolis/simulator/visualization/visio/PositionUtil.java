/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.TimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.MovingAgent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.alite.vis.Vis;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;
import cz.cvut.fel.aic.geographtools.Node;
import cz.cvut.fel.aic.geographtools.util.Transformer;

import javax.vecmath.Point2d;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author fido
 */
@Singleton
public class PositionUtil {
    
    private final Config config;

    private final Map<Integer, ? extends Node> nodesFromAllGraphs;

    private final Graph<SimulationNode, SimulationEdge> network;

    private final TimeProvider timeProvider;
    
    private final Transformer transformer;
    
    private final GraphSpec2D mapSpecification;


    @Inject
    public PositionUtil(AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator,
                        HighwayNetwork highwayNetwork, TimeProvider timeProvider, Config config, GraphSpec2D mapSpecification) {
        this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
        network = highwayNetwork.getNetwork();
        this.timeProvider = timeProvider;
        this.config = config;
        transformer = new Transformer(config.srid);
        this.mapSpecification = mapSpecification;
    }


    public Node getNode(int nodeId) {
        return nodesFromAllGraphs.get(nodeId);
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
        return network.getEdge(entityPositionNodeId, targetNodeId).getLength();
    }


    public int getTripLengthInMeters(GraphTrip<TripItem> graphTrip) {
        int lenght = 0;

        LinkedList<TripItem> locations = graphTrip.getLocations();

        int stratNodeId = locations.get(0).tripPositionByNodeId;
        for (int i = 1; i < locations.size(); i++) {
            int targetNodeId = locations.get(i).tripPositionByNodeId;
            lenght += getEdgeLength(stratNodeId, targetNodeId);
            stratNodeId = targetNodeId;
        }

        return lenght;
    }

    public <A extends Agent & MovingAgent, E extends AgentPolisEntity & TransportableEntity> Point2d
    getCanvasPositionInterpolatedForTransportable(E entity) {
        return getCanvasPositionInterpolatedForTransportable((TransportableEntity) entity);
    }

    private <E extends TransportableEntity> Point2d
    getCanvasPositionInterpolatedForTransportable(E entity) {
        TransportEntity transportEntity = entity.getTransportingEntity();
        if (transportEntity == null) {
            return getCanvasPosition((AgentPolisEntity) entity);
        } else {
            if (transportEntity instanceof MovingAgent) {
                return getCanvasPositionInterpolated((MovingAgent) transportEntity);
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
        else {
            // precise GPS position of the vehicle
            GPSLocation startLocation = vehicle.getPrecisePosition();
            
            // target node - a crossroad for example
            Node targetNode = vehicle.getDriver().getTargetNode();

            // edge length
            double length = getDistance(startLocation, targetNode);
            
            // portion of the edge that is free for ride
            double portion = (length - vehicle.getQueueBeforeVehicleLength()) / length;
            
            // position at the end of the queue
            GPSLocation targetPosition = getPointOnVector(startLocation, targetNode, portion);
            
            return getCanvasPositionInterpolated(startLocation, targetPosition, vehicle.getDriver().getDelayData());
        }
    }

    public <A extends MovingAgent> Point2d getCanvasPositionInterpolated(A entity) {

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

        DelayData delayData = entity.getDelayData();

        return getCanvasPositionInterpolated(startNode, targetNode, delayData);
    }
    
    public Point2d getCanvasPositionInterpolated(GPSLocation startLocation, GPSLocation targetLocation, 
            DelayData delayData) {
         double portionCompleted = (double) (timeProvider.getCurrentSimTime() - delayData.getDelayStartTime())
                / delayData.getDelay();
        
        // if the travel time is longer than expected
        if(portionCompleted > 1){
            portionCompleted = 1;
        }

        Point2d startPosition = getCanvasPosition(startLocation);
        Point2d targetPosition = getCanvasPosition(targetLocation);

        double xIncrement = (targetPosition.x - startPosition.x) * portionCompleted;
        double yIncrement = (targetPosition.y - startPosition.y) * portionCompleted;

        return new Point2d(startPosition.x + xIncrement, startPosition.y + yIncrement);
    }
    
    public GPSLocation getPositionInterpolated(GPSLocation startLocation, GPSLocation targetLocation, 
            DelayData delayData) {
         double portionCompleted = (double) (timeProvider.getCurrentSimTime() - delayData.getDelayStartTime())
                / delayData.getDelay();
        
        if(portionCompleted > 1){
            portionCompleted = 1;
        }

        return getPointOnVector(startLocation, targetLocation, portionCompleted);
    }
    
    public GPSLocation getPointOnEdge(SimulationEdge edge, double portion){
        GPSLocation startPosition = network.getNode(edge.fromId);
        GPSLocation targetPosition = network.getNode(edge.toId);

        return getPointOnVector(startPosition, targetPosition, portion);
    }
    
    public GPSLocation getPointOnVector(GPSLocation startLocation, GPSLocation targetLocation, double portion){
        int xIncrement = (int) Math.round((targetLocation.getLongitudeProjected1E2()
                - startLocation.getLongitudeProjected1E2()) * portion);
        int yIncrement = (int) Math.round((targetLocation.getLatitudeProjected1E2()
                - startLocation.getLatitudeProjected1E2()) * portion);

        return new GPSLocation(0, 0, startLocation.getLatitudeProjected1E2() + yIncrement, 
                startLocation.getLongitudeProjected1E2() + xIncrement);
    }
    
    public double getDistance(GPSLocation from, GPSLocation to){
        return Math.sqrt(Math.pow(Math.abs(from.getLatitudeProjected()- to.getLatitudeProjected()), 2) 
                + Math.pow(Math.abs(from.getLongitudeProjected() - to.getLongitudeProjected()), 2));
    }
}
