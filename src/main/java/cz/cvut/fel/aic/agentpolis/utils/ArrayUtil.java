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

import java.util.Arrays;

/**
 *
 * @author david
 */
public class ArrayUtil {
	public static int[] addArrays(int[] a, int[] b){
		int result[] = new int[a.length];
		Arrays.setAll(result, i -> a[i] + b[i]);
		return result;
	}
	
	public static int[] addArraysInPlace(int[] a, int[] b){
		if(a.length == b.length){
			Arrays.setAll(a, i -> a[i] + b[i]);
		}
		else{
			for (int i = 0; i < b.length; i++) {
				a[i] += b[i];
			}
		}
		return a;
	}
}
