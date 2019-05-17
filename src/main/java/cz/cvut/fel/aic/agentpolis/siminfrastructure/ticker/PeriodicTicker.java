/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.siminfrastructure.ticker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.utils.CollectionUtil;
import cz.cvut.fel.aic.agentpolis.utils.MathUtil;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.alite.common.event.typed.TypedSimulation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fido
 */
@Singleton
public class PeriodicTicker extends EventHandlerAdapter{
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PeriodicTicker.class);
	
    private final TypedSimulation simulation;
	
	private final Map<Integer, List<Routine>> routineMap;
    
    private int tickLength;
	
	private long nextTickTime;

    
    
    @Inject
    public PeriodicTicker(TypedSimulation simulation) {
        this.simulation = simulation;
		routineMap = new HashMap();
		tickLength = Integer.MAX_VALUE;
    }

    
	public void registerRoutine(Routine routine, int period){
		CollectionUtil.addToListInMap(routineMap, period, routine);
		updateTick();
		LOGGER.info("{} registered to run with period of {} ms", routine.getClass(), period);
	}

    @Override
    public void handleEvent(Event event) {
		long simulationTime = simulation.getCurrentTime();
		
		// check if tick wasn't updated
		if(simulationTime != nextTickTime){
			return;
		}
		
        for(Map.Entry<Integer,List<Routine>> entry: routineMap.entrySet()){
			if(simulationTime % entry.getKey() == 0){
				executeRoutines(entry.getValue());
			}
		}
		
		nextTickTime += tickLength;
        createTick(tickLength);
    }


	private void updateTick() {
		int gcd = MathUtil.gcd(routineMap.keySet().stream().mapToInt(Number::intValue).toArray());
		if(gcd < tickLength){
			tickLength = gcd;
			
			// next tick event
			long simulationTime = simulation.getCurrentTime();
			long nearestSyncTime = (simulationTime + gcd) - (simulationTime % gcd);
			if(nextTickTime != nearestSyncTime){
				nextTickTime = nearestSyncTime;
				long timeToNearestTick = nearestSyncTime - simulationTime;
				createTick(timeToNearestTick);
			}
		}
				
	}

	private void createTick(long timeToNearestTick) {
		simulation.addEvent(TickEvent.TICK, this, null, null, timeToNearestTick);
	}

	private void executeRoutines(List<Routine> routines) {
		for(Routine routine: routines){
			routine.doRoutine();
		}
	}
	
	private enum TickEvent{
		TICK;
	}
}
