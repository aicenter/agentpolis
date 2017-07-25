/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.entity;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.Projection;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

import javax.vecmath.Point2d;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author F-I-D-O
 */
public class EntityPositionUtil {
    protected final PositionUtil positionUtil;
    
	protected final EntityPositionModel entityPositionModel;
	
	protected final Map<Integer, ? extends Node> nodesFromAllGraphs;
	
	private final Projection projection;
	
	private final EntityStorage entityStorage;
    
    private final Graph<SimulationNode,SimulationEdge> network;
    
    

    @Inject
	public EntityPositionUtil(PositionUtil positionUtil, EntityPositionModel entityPositionModel, 
            AllNetworkNodes allNetworkNodes, Projection projection, EntityStorage entityStorage,
            HighwayNetwork highwayNetwork) {
        this.positionUtil = positionUtil;
		this.entityPositionModel = entityPositionModel;
		this.nodesFromAllGraphs = allNetworkNodes.getAllNetworkNodes();
		this.projection = projection;
		this.entityStorage = entityStorage;
        network = highwayNetwork.getNetwork();
	}
	
	public Point2d getEntityPosition(AgentPolisEntity entity){
        Node entityPositionNode = getEntityNodePosition(entity);
        
		return positionUtil.getPosition(entityPositionNode);
	}
    
    public Node getEntityNodePosition(AgentPolisEntity entity){
        if(entityPositionModel.getEntityPositionByNodeId(entity.getId()) == null){
            return null;
        }
        
        return nodesFromAllGraphs.get(
					entityPositionModel.getEntityPositionByNodeId(entity.getId()));
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
