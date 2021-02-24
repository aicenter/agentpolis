/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.utils;

import java.util.LinkedList;
import java.util.List;
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
	
	public static <K> void incrementMapValue(Map<K,Long> map, K key, long increment){
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
	
	public static <K,V> void addToListInMap(Map<K,List<V>> map, K key, V value){
		if(!map.containsKey(key)){
			map.put(key, new LinkedList<>());
		}
		map.get(key).add(value);
	}
}
