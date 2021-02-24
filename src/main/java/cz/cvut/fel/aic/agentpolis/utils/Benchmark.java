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

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Benchmark util - it is not thread safe!
 * @author david
 */
public class Benchmark {
	
	private static final int MILLION = 1000000;
	
	
	public long durationNano;
	
	public long getDurationMs(){
		return durationNano / MILLION;
	}
	
	public int getDurationMsInt(){
		return (int) (durationNano / MILLION);
	}
	

	
	public <P,R> R measureTime(Function<P,R> measuredMethod, P param){
		long startTimeNano = System.nanoTime();
		R returnValue = measuredMethod.apply(param);
		durationNano = System.nanoTime() - startTimeNano;
		return returnValue;
	}
	
	public <R> R measureTime(Supplier<R> measuredMethod){
		return measureTime((Object dummyIn) -> {return measuredMethod.get();}, null);
	}
	
	public void measureTime(Runnable measuredMethod){
		measureTime(() -> {measuredMethod.run();return null;});
	}

}
