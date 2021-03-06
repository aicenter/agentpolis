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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * @author Zdenek Bousa
 */
public class Lane {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Lane.class);
	private final long laneUniqueId;
	private final String parentEdgeUniqueId;
	private List<Integer> heading = new LinkedList<>();


	public Lane(long laneUniqueId, String parentEdgeUniqueId) {
		LOGGER.warn("NotImplementedException", new NotImplementedException());
		this.laneUniqueId = laneUniqueId;
		this.parentEdgeUniqueId = parentEdgeUniqueId;
	}

	public int[] getHeadingIds() {
		return null;
	}

	public void addHeadingEdgeId(int headingEdgeID) {

	}

	public long getLaneUniqueId() {
		return laneUniqueId;
	}

	public String getParentEdgeUniqueId() {
		return parentEdgeUniqueId;
	}

	@Singleton
	public static class LaneBuilder {
		private long laneCounter = 0;

		public Lane createNewLane(String parent) {
			return new Lane(0, "");
		}

		public LaneBuilder getBuilder() {
			return this;
		}
	}
}
