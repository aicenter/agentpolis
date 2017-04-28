/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.trip.GraphTrip;
import cz.agents.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.Agent;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.entity.MovingEntity;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import cz.agents.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.alite.vis.Vis;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;
import java.util.LinkedList;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import java.util.Map;

/**
 *
 * @author fido
 */
@Singleton
public class PositionUtil {
    
    private final Projection projection;
	
	private final Map<Integer, ? extends Node> nodesFromAllGraphs;
    
    private final BoundingBox mapBounds;
    
    private final Graph<SimulationNode,SimulationEdge> network;
    
    private final TimeProvider timeProvider;

    
    
    @Inject
    public PositionUtil(Projection projection, AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator,
            HighwayNetwork highwayNetwork, TimeProvider timeProvider) {
        this.projection = projection;
		this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
        mapBounds = simulationCreator.getBoundsOfMap();
        network = highwayNetwork.getNetwork();
        this.timeProvider = timeProvider;
    }
    
    
    public Node getNode(int nodeId){
        return nodesFromAllGraphs.get(nodeId);
    }
    
    public Point2d getPosition(GPSLocation position){
        Point3d projectedPoint = projection.project(position);
        return new Point2d(projectedPoint.x, projectedPoint.y);
    }
    
    public Point2d getCanvasPosition(GPSLocation position){
        Point3d projectedPoint = projection.project(position);
        return new Point2d(Vis.transX(projectedPoint.x), Vis.transY(projectedPoint.y));
    }
	
	public Point2d getCanvasPosition(int nodeId){
        return getCanvasPosition(nodesFromAllGraphs.get(nodeId));
    }
    
    public Point2d getCanvasPosition(AgentPolisEntity entity){
        return getCanvasPosition(entity.getPosition());
    }
    
    public Point2d getCanvasPosition(Point2d position){
        return new Point2d(Vis.transX(position.x), Vis.transY(position.y));
    }
    
    public int getWorldWidth(){
        Point2d minMin = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMinLonE6(), 0, 0));
        Point2d minMax = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMaxLonE6(), 0, 0));
        
        return (int) (minMax.x - minMin.x);
    }
    
     public int getWorldHeight(){
        Point2d minMin = getPosition(new GPSLocation(mapBounds.getMinLatE6(), mapBounds.getMinLonE6(), 0, 0));
        Point2d maxMin = getPosition(new GPSLocation(mapBounds.getMaxLatE6(), mapBounds.getMinLonE6(), 0, 0));
        
        return (int) (minMin.y - maxMin.y);
    }
     
    private int getEdgeLength(int entityPositionNodeId, int targetNodeId) {
        return network.getEdge(entityPositionNodeId, targetNodeId).getLength();
    }
     
         
    public int getTripLengthInMeters(GraphTrip<TripItem> graphTrip){
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
    
    public <A extends Agent & TransportAgent, E extends AgentPolisEntity & TransportableEntity>Point2d 
            getCanvasPositionInterpolatedForTransportable(E entity){
        A transportAgent = entity.getTransportingAgent();
        if(transportAgent == null){
            return getCanvasPosition(entity);
        }
        else{
            return getCanvasPositionInterpolated(transportAgent);
        }
    }
    
    public <E extends AgentPolisEntity & MovingEntity> Point2d getCanvasPositionInterpolated(E entity){
        
        Node startNode = entity.getPosition();
        
        Node targetNode = entity.getTargetNode();
        
        // vehicle waits 
        if(targetNode == null){
            return getCanvasPosition(startNode);
        }
        
        DelayData delayData = entity.getDelayData();
        
        // vehicle has no delay yet
//        if(delayData == null){
//            return positionUtil.getCanvasPosition(entityPositionNode);
//        }
        
        double portionCompleted = (double) (timeProvider.getCurrentSimTime() - delayData.getDelayStartTime()) 
                / delayData.getDelay();
        
        Point2d startPosition = getCanvasPosition(startNode);
        Point2d targetPosition = getCanvasPosition(targetNode);
        
        double xIncrement = (targetPosition.x - startPosition.x) * portionCompleted;
        double yIncrement = (targetPosition.y - startPosition.y) * portionCompleted;
        
        return new Point2d(startPosition.x + xIncrement, startPosition.y + yIncrement);
	}
}
