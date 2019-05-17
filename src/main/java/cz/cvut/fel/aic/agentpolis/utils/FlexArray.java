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

/**
 *
 * @author david
 */
public class FlexArray {
	private int[] array;

	public FlexArray() {
		this(0);
	}
	
	public FlexArray(int size) {
		array = new int[size];
		for (int i = 0; i < array.length; i++) {
			array[i] = 0;	
		}	
	}
	
	
	
	public void increment(int index){
		increment(index, 1);
	}
	
	public void increment(int index, int increment){
		if(index >= array.length){
			enlarge();
		}
		array[index] += increment;
	}
	
	public int[] addArrayInPlace(FlexArray b){
		if(b.array.length > array.length){
			enlarge(b.array.length);
		}
		return ArrayUtil.addArraysInPlace(array, b.array);
	}
	
	public int size(){
		return array.length;
	}
	
	public int get(int index){
		return array[index];
	}
	
	private void enlarge(){
		enlarge(array.length + 1);
	}
	
	private void enlarge(int to){
		int[] old = array;
		array = new int[to];
		for (int i = 0; i < to; i++) {
			if(i < old.length){
				array[i] = old[i];
			}
			else{
				array[i] = 0;
			}
		}
	}
}
