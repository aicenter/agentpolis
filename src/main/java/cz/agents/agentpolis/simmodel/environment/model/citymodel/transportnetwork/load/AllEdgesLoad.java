/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.load;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.CollectionUtil;
import cz.agents.agentpolis.simmodel.agent.TransportAgent;
import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simmodel.environment.model.EntityStorage;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.HighwayNetwork;
import cz.agents.basestructures.Graph;
import cz.agents.basestructures.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author fido
 * @param <E>
 * @param <ES>
 */
public class AllEdgesLoad<E extends AgentPolisEntity & TransportAgent, ES extends EntityStorage<E>> 
        implements Iterable<Entry<String,Integer>>{
    
    private final ES entityStorage;
    
    private final HashMap<String,Integer> loadPerEdge;
    
    private final Graph<SimulationNode,SimulationEdge> network;

    
    public Iterable<Integer> loadsIterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return loadPerEdge.values().iterator();
        }
    };
    
//    public ArrayList<Integer> test;
    
    
    
    
    public HashMap<String, Integer> getLoadPerEdge() {
        return loadPerEdge;
    }
    
    
    
    

    @Inject
    public AllEdgesLoad(ES entityStorage, HighwayNetwork highwayNetwork) {
        this.entityStorage = entityStorage;
        loadPerEdge = new HashMap<>();
        network = highwayNetwork.getNetwork();
    }
    
    
    

    public void compute(){
        for (E entity : entityStorage) {
            String entityId = entity.getId();
            Node currentNode = entity.getPosition();
            Node targetNode = entity.getTargetNode();
            if(targetNode != null && !targetNode.equals(currentNode)){
                String edgeId = network.getEdge(currentNode.id, targetNode.id).getUniqueID();
                countLoadForPosition(entityId, edgeId);
            }
        }
    }

    public int getLoadPerEdge(String uniqueID) {
        if (loadPerEdge.containsKey(uniqueID)) {
            return loadPerEdge.get(uniqueID);
        }
        return 0;
    }
    
    public int getNumberOfEdges(){
        return network.getAllEdges().size();
    }
    

    @Override
    public Iterator<Entry<String, Integer>> iterator() {
        return loadPerEdge.entrySet().iterator();
    }
    
    

    protected void countLoadForPosition(String entityId, String edgeId) {
        CollectionUtil.incrementMapValue(loadPerEdge, edgeId, 1);
    }
    
    
}
