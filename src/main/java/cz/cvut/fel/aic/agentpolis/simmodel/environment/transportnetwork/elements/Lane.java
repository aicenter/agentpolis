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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements;

import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zdenek Bousa
 */
public class Lane {
    private static final Logger LOGGER = Logger.getLogger(Lane.class);
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
