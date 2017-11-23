/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.siminfrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    
    public static <K,V> Map.Entry<K,V> getRandomEntryFromMap(Map<K,V> map, Random randomGenerator) {
        Object[] entries = map.entrySet().toArray();
        Object randomEntry = entries[randomGenerator.nextInt(entries.length)];
        return (Map.Entry<K,V>) randomEntry;
    }
}
