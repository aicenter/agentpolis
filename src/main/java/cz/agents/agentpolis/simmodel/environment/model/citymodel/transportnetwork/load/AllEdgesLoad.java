/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.load;

import com.google.inject.Inject;
import cz.agents.agentpolis.siminfrastructure.CollectionUtil;
import cz.agents.agentpolis.simmodel.agent.MovingAgent;
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
public class AllEdgesLoad<E extends AgentPolisEntity & MovingAgent, ES extends EntityStorage<E>> 
        implements Iterable<Entry<Integer,Integer>>{
    
    protected final ES entityStorage;
    
    private final HashMap<Integer,Integer> loadPerEdge;
    
    protected final Graph<SimulationNode,SimulationEdge> network;

    
    public Iterable<Integer> loadsIterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return loadPerEdge.values().iterator();
        }
    };
    
//    public ArrayList<Integer> test;
    
    
    
    
    public HashMap<Integer, Integer> getLoadPerEdge() {
        return loadPerEdge;
    }
    
    
    
    

    @Inject
    public AllEdgesLoad(ES entityStorage, HighwayNetwork highwayNetwork) {
        this.entityStorage = entityStorage;
        loadPerEdge = new HashMap<>();
        network = highwayNetwork.getNetwork();
    }
    
    
    
    @Inject // this annotation is necessary because this method has to be called after child constructor finishes
    public void compute(){
        for (E entity : entityStorage) {
            String entityId = entity.getId();
            Node currentNode = entity.getPosition();
            Node targetNode = entity.getTargetNode();
            if(targetNode != null && !targetNode.equals(currentNode)){
                int edgeId = network.getEdge(currentNode.id, targetNode.id).getUniqueId();
                countLoadForPosition(entityId, edgeId);
            }
        }
    }

    public int getLoadPerEdge(Integer uniqueID) {
        if (loadPerEdge.containsKey(uniqueID)) {
            return loadPerEdge.get(uniqueID);
        }
        return 0;
    }
    
    public int getNumberOfEdges(){
        return network.getAllEdges().size();
    }
    

    @Override
    public Iterator<Entry<Integer, Integer>> iterator() {
        return loadPerEdge.entrySet().iterator();
    }
    
    

    protected void countLoadForPosition(String entityId, int edgeId) {
        CollectionUtil.incrementMapValue(loadPerEdge, edgeId, 1);
    }
    
    
}
