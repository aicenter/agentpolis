/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.agents.agentpolis.simulator.creator.SimulationCreator;
import cz.agents.alite.vis.Vis;
import cz.agents.basestructures.BoundingBox;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Node;

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

    
    
    @Inject
    public PositionUtil(Projection projection, AllNetworkNodes allNetworkNodes, SimulationCreator simulationCreator) {
        this.projection = projection;
		this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
        mapBounds = simulationCreator.getBoundsOfMap();
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
}
