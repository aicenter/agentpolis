/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simulator.visualization.visio.entity;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.alite.vis.Vis;
import cz.agents.basestructures.Node;
import java.util.Iterator;
import java.util.Map;
import javafx.geometry.Point2D;

/**
 *
 * @author F-I-D-O
 */
public class EntityPositionUtil {
	private final EntityPositionModel entityPositionModel;
	
	private final Map<Integer, ? extends Node> nodesFromAllGraphs;
	
	private final Projection projection;
	
	private final EntityStorage entityStorage;

	public EntityPositionUtil(EntityPositionModel entityPositionModel, Map<Integer, ? extends Node> nodesFromAllGraphs, 
			Projection projection, EntityStorage entityStorage) {
		this.entityPositionModel = entityPositionModel;
		this.nodesFromAllGraphs = nodesFromAllGraphs;
		this.projection = projection;
		this.entityStorage = entityStorage;
	}
	
	private Point2D getEntityPosition(AgentPolisEntity entity){
		return new Point2D(Vis.transX(projection.project(nodesFromAllGraphs.get(
					entityPositionModel.getEntityPositionByNodeId(entity.getId()))).x), 
				Vis.transY(projection.project(nodesFromAllGraphs.get(
						entityPositionModel.getEntityPositionByNodeId(entity.getId()))).y));
	}
	
	public class EntityPositionIterator{
		
		private final Iterator<String> idIterator;

		public EntityPositionIterator() {
			idIterator = entityStorage.getEntityIds().iterator();
		}
		
		public Point2D getNextEntityPosition(){
			while(idIterator.hasNext()){
				AgentPolisEntity entity = entityStorage.getEntityById(idIterator.next());
				if(entityPositionModel.getEntityPositionByNodeId(entity.getId()) == null){
					continue;
				}
				return getEntityPosition(entity);
			}
			return null;
		}
		
	}
}
