/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.vividsolutions.jts.geom.Coordinate;
import cz.agents.agentpolis.utils.nearestelement.NearestElementUtil;
import cz.agents.basestructures.GPSLocation;
import cz.agents.basestructures.Node;
import cz.agents.geotools.Transformer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.javatuples.Pair;

/**
 *
 * @author fido
 */
@Singleton
public class NearestElementUtils {
    private final HashMap<GraphType, NearestElementUtil> nearestElementUtilsMappedByGraphType;
    
    private final TransportNetworks transportNetworks;
    
    private final Transformer transformer;
    
//    private final Map<GraphType,Network> networksMappedByGraphType;

    @Inject
    public NearestElementUtils(TransportNetworks transportNetworks, @Named("mapSrid") int srid) {
        this.transportNetworks = transportNetworks;
        this.transformer = new Transformer(srid);
//        this.networksMappedByGraphType = new HashMap<>();
        this.nearestElementUtilsMappedByGraphType = new HashMap<>();
    }
    
    public <E> E getNearestElement(GPSLocation location, GraphType graphType){
        if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
            createNearestElementUtil(graphType);
        }
        
        NearestElementUtil<E> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
        
        return nearestElementUtil.getNearestElement(location);
    }
    
    public <E> E[] getNearestElements(GPSLocation location, GraphType graphType, int numberOfNearestElements){
        if(!nearestElementUtilsMappedByGraphType.containsKey(graphType)){
            createNearestElementUtil(graphType);
        }
        
        NearestElementUtil<E> nearestElementUtil = nearestElementUtilsMappedByGraphType.get(graphType);
        
        return (E[]) nearestElementUtil.getKNearestElements(location, numberOfNearestElements);
    }

    private void createNearestElementUtil(GraphType graphType) {
        List<Pair<Coordinate,Node>> pairs = new ArrayList<>();
		
		for (Node node : transportNetworks.getGraph(graphType).getAllNodes()) {
			pairs.add(new Pair<>(new Coordinate(node.getLongitude(), node.getLatitude()), node));
		}
		
		NearestElementUtil<Node> nearestElementUtil = new NearestElementUtil<>(pairs, transformer, 
				new NodeArrayConstructor());
        
        nearestElementUtilsMappedByGraphType.put(graphType, nearestElementUtil);
    }
    
    private static class NodeArrayConstructor implements NearestElementUtil.SerializableIntFunction<Node[]>{

        @Override
        public Node[] apply(int value) {
            return new Node[value];
        }

    }
    
    
    
}
