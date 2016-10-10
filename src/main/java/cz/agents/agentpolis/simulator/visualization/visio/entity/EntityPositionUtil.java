/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.AllNetworkNodes;
import cz.agents.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.basestructures.Node;
import java.util.Iterator;
import java.util.Map;
import javax.vecmath.Point2d;


/**
 *
 * @author F-I-D-O
 */
public class EntityPositionUtil {
    private final PositionUtil positionUtil;
    
	private final EntityPositionModel entityPositionModel;
	
	private final Map<Integer, ? extends Node> nodesFromAllGraphs;
	
	private final Projection projection;
	
	private final EntityStorage entityStorage;

    @Inject
	public EntityPositionUtil(PositionUtil positionUtil, EntityPositionModel entityPositionModel, 
            AllNetworkNodes allNetworkNodes, Projection projection, EntityStorage entityStorage) {
        this.positionUtil = positionUtil;
		this.entityPositionModel = entityPositionModel;
		this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
		this.projection = projection;
		this.entityStorage = entityStorage;
	}
	
	public Point2d getEntityCanvasPosition(AgentPolisEntity entity){
        if(entityPositionModel.getEntityPositionByNodeId(entity.getId()) == null){
            return null;
        }
        
        Node entityPositionNode = nodesFromAllGraphs.get(
					entityPositionModel.getEntityPositionByNodeId(entity.getId()));
        
		return positionUtil.getCanvasPosition(entityPositionNode);
	}
	
	public class EntityPositionIterator{
		
		private final Iterator<String> idIterator;

		public EntityPositionIterator() {
			idIterator = entityStorage.getEntityIds().iterator();
		}
		
		public Point2d getNextEntityPosition(){
			while(idIterator.hasNext()){
				AgentPolisEntity entity = entityStorage.getEntityById(idIterator.next());
				if(entityPositionModel.getEntityPositionByNodeId(entity.getId()) == null){
					continue;
				}
				return getEntityCanvasPosition(entity);
			}
			return null;
		}
		
	}
}
