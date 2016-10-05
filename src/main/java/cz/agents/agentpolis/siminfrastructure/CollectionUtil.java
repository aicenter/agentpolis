/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.siminfrastructure;

import java.util.Map;

/**
 *
 * @author fido
 */
public class CollectionUtil {
    
    public static <K> void incrementMapValue(Map<K,Integer> map, K key, int increment){
		if(map.containsKey(key)){
			map.put(key, map.get(key) + increment);
		}
		else{
			map.put(key, increment);
		}
	}
}
