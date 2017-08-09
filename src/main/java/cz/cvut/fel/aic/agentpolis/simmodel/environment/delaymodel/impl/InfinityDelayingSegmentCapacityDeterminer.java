package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.impl;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.factory.DelayingSegmentCapacityDeterminer;

/**
 * 
 * The mock of capacity per road/rail segment. For each segment is set on max
 * value - it is prevention against deadlock on road/rail. The reason of
 * deadlock is using simplified world.
 * 
 * @author Zbynek Moler
 * 
 */
public class InfinityDelayingSegmentCapacityDeterminer implements DelayingSegmentCapacityDeterminer {

	@Override
	public double determineDelaySegmentCapacity(double maxCapacity) {
		return Double.MAX_VALUE;
	}

}
