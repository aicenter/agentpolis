/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.CollectionUtil;
import cz.agents.agentpolis.simmodel.environment.model.AgentPositionModel;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.HighwayNetwork;
import cz.agents.basestructures.Graph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author fido
 */
public class AllEdgesLoad implements Iterable<Entry<Long,Integer>>{
    
    private final HashMap<Long,Integer> loadPerEdge;
    
    private final Map<String,Integer> positions;
    
    private final Map<String,Integer> targetPositions;
    
    private final Graph<SimulationNode,SimulationEdge> network;

    
    
    
    public HashMap<Long, Integer> getLoadPerEdge() {
        return loadPerEdge;
    }
    
    
    
    

    @Inject
    public AllEdgesLoad(AgentPositionModel entityPositionModel, HighwayNetwork highwayNetwork) {
        loadPerEdge = new HashMap<>();
        positions = entityPositionModel.getCurrentPositions();
        targetPositions = entityPositionModel.getCurrentTargetPositions();
        network = highwayNetwork.getNetwork();
        
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            String key = entry.getKey();
            Integer currentNodeId = entry.getValue();
            Integer targetNodeId = targetPositions.get(key);
            if(targetNodeId != null && !targetNodeId.equals(currentNodeId)){
                CollectionUtil.incrementMapValue(loadPerEdge, network.getEdge(currentNodeId, targetNodeId).wayID, 1);
            }
        }
    }

    public int getLoadPerEdge(long wayID) {
        if(loadPerEdge.containsKey(wayID)){
            return loadPerEdge.get(wayID);
        }
        return 0;
    }
    
    public int getNumberOfEdges(){
        return network.getAllEdges().size();
    }
    

    @Override
    public Iterator<Entry<Long, Integer>> iterator() {
        return loadPerEdge.entrySet().iterator();
    }
    
    public Iterable<Integer> loadsIterator = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return loadPerEdge.values().iterator();
        }
    };
    
    
    
}
