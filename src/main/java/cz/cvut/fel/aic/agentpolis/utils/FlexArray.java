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
			enlarge(index + 1);
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
