/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.VehiclePositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.driving.DelayData;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.action.driving.MoveVehicleAction;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.basestructures.Node;

import javax.vecmath.Point2d;

/**
 *
 * @author fido
 */
@Singleton
public class VehiclePositionUtil extends EntityPositionUtil{
    
    private final MoveVehicleAction moveVehicleAction;
    
    private final StandardTimeProvider timeProvider;
    
    private final AgentPositionModel agentPositionModel;
    
    
    @Inject
    public VehiclePositionUtil(PositionUtil positionUtil, VehiclePositionModel entityPositionModel, 
            AllNetworkNodes allNetworkNodes, Projection projection, EntityStorage entityStorage, 
            HighwayNetwork highwayNetwork, MoveVehicleAction moveVehicleAction, StandardTimeProvider timeProvider, 
            AgentPositionModel agentPositionModel) {
        super(positionUtil, entityPositionModel, allNetworkNodes, projection, entityStorage, highwayNetwork);
        this.moveVehicleAction = moveVehicleAction;
        this.timeProvider = timeProvider;
        this.agentPositionModel = agentPositionModel;
    }
    
    
    
    
    public Point2d getVehicleCanvasPositionInterpolated(PhysicalVehicle vehicle, Agent driver){
        if(entityPositionModel.getEntityPositionByNodeId(vehicle.getId()) == null){
            return null;
        }
        
        Node entityPositionNode = nodesFromAllGraphs.get(
					entityPositionModel.getEntityPositionByNodeId(vehicle.getId()));
        
        Integer targetNodeId = agentPositionModel.getEntityTargetPositionByNodeId(driver.getId());
        
        // vehicle waits 
        if(targetNodeId == null){
            return positionUtil.getCanvasPosition(entityPositionNode);
        }
        
//		int currentEdgeLegth = getEdgeLength(entityPositionNode.getId(), targetNodeId);
        
        DelayData delayData = moveVehicleAction.getDelayDataForVehicle(vehicle.getId());
        
        // vehicle has no delay yet
        if(delayData == null){
            return positionUtil.getCanvasPosition(entityPositionNode);
        }
        
        double portionCompleted = (double) (timeProvider.getCurrentSimTime() - delayData.getDelayStartTime()) 
                / delayData.getDelay();
        
        Node targetNode = nodesFromAllGraphs.get(targetNodeId);
        
        Point2d startPosition = positionUtil.getCanvasPosition(entityPositionNode);
        Point2d targetPosition = positionUtil.getCanvasPosition(targetNode);
        
        double xIncrement = (targetPosition.x - startPosition.x) * portionCompleted;
        double yIncrement = (targetPosition.y - startPosition.y) * portionCompleted;
        
        return new Point2d(startPosition.x + xIncrement, startPosition.y + yIncrement);
	}
}
