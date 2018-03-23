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
package cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.lanes;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.congestion.agent.CongestedTripData;

/**
 * Congestion queue for each lane
 */
public class CongestionQueueData {
    private final CongestedTripData congestedTripData;
    private long minPollTime;

    public CongestionQueueData(CongestedTripData congestedTripData, long minPollTime) {
        this.congestedTripData = congestedTripData;
        this.minPollTime = minPollTime;
    }
    public CongestedTripData getCongestedTripData() {
        return congestedTripData;
    }

    /**
     * When to poll
     * @return ms
     */
    public long getMinPollTime() {
        return minPollTime;
    }

    /**
     * Set time to poll
     * @param minPollTime ms
     */
    public void setMinPollTime(long minPollTime) {
        this.minPollTime = minPollTime;
    }
}