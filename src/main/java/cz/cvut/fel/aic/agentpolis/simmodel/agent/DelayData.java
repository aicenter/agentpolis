/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
