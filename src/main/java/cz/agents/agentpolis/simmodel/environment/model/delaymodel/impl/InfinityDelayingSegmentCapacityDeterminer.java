package cz.agents.agentpolis.simmodel.environment.model.delaymodel.impl;

import cz.agents.agentpolis.simmodel.environment.model.delaymodel.factory.DelayingSegmentCapacityDeterminer;

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
