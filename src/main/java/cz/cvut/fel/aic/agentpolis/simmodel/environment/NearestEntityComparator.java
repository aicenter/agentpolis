package cz.cvut.fel.aic.agentpolis.simmodel.environment;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import java.util.Comparator;
import javax.vecmath.Point2d;

/**
 *
 * @author F.I.D.O.
 * @param <E> entity type
 */
public class NearestEntityComparator<E extends AgentPolisEntity> 
        implements Comparator<E>{
	
	protected final Point2d from;
	
	protected final PositionUtil positionUtil;

	
	public NearestEntityComparator(PositionUtil positionUtil, Point2d from) {
		this.from = from;
		this.positionUtil = positionUtil;
	}
	
	

	@Override
	public int compare(AgentPolisEntity e1, AgentPolisEntity e2) {
		return Double.compare(positionUtil.getPosition(e1.getPosition()).distance(from), 
				positionUtil.getPosition(e2.getPosition()).distance(from));
	}
	
}
