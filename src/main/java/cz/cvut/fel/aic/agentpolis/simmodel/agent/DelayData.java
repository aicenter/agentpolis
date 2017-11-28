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
package cz.cvut.fel.aic.agentpolis.simmodel.agent;

/**
 * @author fido
 */
public class DelayData {
    private final Long delay;
    private final Long delayStartTime;
    private final Double startDistanceOffset;
    private final Double delayDistance;

    public DelayData(Long delay, Long delayStartTime, Double delayDistance) {
        this(delay, delayStartTime, delayDistance, 0.0);
    }

    public DelayData(Long delay, Long delayStartTime, Double delayDistance, Double startDistanceOffset) {
        this.delay = delay;
        this.delayStartTime = delayStartTime;
        this.startDistanceOffset = startDistanceOffset;
        this.delayDistance = delayDistance;
    }

    public Long getDelay() {
        return delay;
    }

    public Long getDelayStartTime() {
        return delayStartTime;
    }


    public Double getStartDistanceOffset() {
        return startDistanceOffset;
    }

    public Double getDelayDistance() {
        return delayDistance;
    }
}
