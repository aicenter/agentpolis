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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.util.AngleUtil;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EdgeShape implements Iterable<GPSLocation>, Serializable {

	private static final long serialVersionUID = -837444965512183266L;

	final List<GPSLocation> backingMap;
	double[] segmentAngles;
	double[] segmentCumulativeLength;
	double shapeLength;

	public EdgeShape(List<GPSLocation> backingMap) {
		if (backingMap == null || backingMap.size() < 2)
			throw new IllegalArgumentException();
		this.backingMap = backingMap;
		segmentAngles = new double[backingMap.size() - 1];
		segmentCumulativeLength = new double[backingMap.size() - 1];
		for (int i = 1; i < backingMap.size(); i++) {
			GPSLocation a = backingMap.get(i - 1);
			GPSLocation b = backingMap.get(i);
			shapeLength += GPSLocationTools.computeDistanceAsDouble(a, b);
			segmentCumulativeLength[i - 1] = shapeLength;
			segmentAngles[i - 1] = AngleUtil.computeAngle(a, b);
		}
	}

	public GPSLocation from() {
		return backingMap.get(0);
	}

	public GPSLocation to() {
		return backingMap.get(backingMap.size() - 1);
	}

	public int size() {
		return backingMap.size();
	}

	@Override
	public Iterator<GPSLocation> iterator() {
		return backingMap.iterator();
	}

	@Override
	public void forEach(Consumer<? super GPSLocation> consumer) {
		backingMap.forEach(consumer);
	}

	@Override
	public Spliterator<GPSLocation> spliterator() {
		return backingMap.spliterator();
	}

	public Stream<GPSLocation> stream() {
		return backingMap.stream();
	}

	public double getShapeLength() {
		return shapeLength;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EdgeShape)) return false;

		EdgeShape that = (EdgeShape) o;

		if (Double.compare(that.shapeLength, shapeLength) != 0) return false;
		if (!backingMap.equals(that.backingMap)) return false;
		if (!Arrays.equals(segmentAngles, that.segmentAngles)) return false;
		return Arrays.equals(segmentCumulativeLength, that.segmentCumulativeLength);
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = backingMap.hashCode();
		result = 31 * result + Arrays.hashCode(segmentAngles);
		result = 31 * result + Arrays.hashCode(segmentCumulativeLength);
		temp = Double.doubleToLongBits(shapeLength);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
