package cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.factory;

import java.util.HashSet;
import java.util.LinkedList;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayActor;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.delaymodel.DelayingSegment;

/**
 * Factory for creation instance of {@code DelayingSegment}. 
 * 
 * @author Zbynek Moler
 *
 */
public class DelayingSegmentFactory {

	public DelayingSegment createDelayingSegmentInstance(double maxCapacity){
		
		assert maxCapacity > 0 : "Value of maxCapacity has to be grater then zero";
		
		return new DelayingSegment(maxCapacity, new LinkedList<DelayActor>(),new LinkedList<DelayActor>(),new HashSet<String>());
		
	}
	
}
