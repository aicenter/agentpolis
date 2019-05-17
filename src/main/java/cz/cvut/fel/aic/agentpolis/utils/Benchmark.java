/*
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.utils;

import java.util.function.Supplier;

/**
 * Benchmark util - it is not thread safe!
 * @author david
 */
public class Benchmark {
	
	private static final int MILLION = 1000000;
	
	
	public static long durationNano;
	
	public static long getDurationMs(){
		return durationNano / MILLION;
	}
	
	public static int getDurationMsInt(){
		return (int) (durationNano / MILLION);
	}
	
	public static <T> T measureTime(Supplier<T> measuredMethod){
		long startTimeNano = System.nanoTime();
		T returnValue = measuredMethod.get();
		durationNano = System.nanoTime() - startTimeNano;
		return returnValue;
	}
	
	public static void measureTime(Runnable measuredMethod){
		measureTime(() -> {measuredMethod.run();return null;});
	}
	
//	public interface MeasuredMethod{
//		public Object call();
//	}
}