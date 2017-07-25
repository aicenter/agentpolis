/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.citymodel.transportnetwork.load;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.CollectionUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.EntityPositionModel.EntityNodePositionIterator;

import java.util.HashMap;

/**
 *
 * @author fido
 */
public class AllNodesLoad {
    private final HashMap<Integer,Integer> loadPerNodeMap;


    

    @Inject
    public AllNodesLoad(EntityPositionModel entityPositionModel) {
        this.loadPerNodeMap = new HashMap<>();
        EntityNodePositionIterator iterator = entityPositionModel.new EntityNodePositionIterator();
        Integer nodeId;
        while((nodeId = iterator.getNextEntityNodeId()) != null){
            CollectionUtil.incrementMapValue(loadPerNodeMap, nodeId, 1);
        }
    }
    
    
    
    
    
    public int getLoadPerNode(int nodeId) {
        if(loadPerNodeMap.containsKey(nodeId)){
            return loadPerNodeMap.get(nodeId);
        }
        return 0;
    }
    
}
