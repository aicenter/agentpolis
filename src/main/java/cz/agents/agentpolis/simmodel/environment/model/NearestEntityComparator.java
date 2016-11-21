package cz.agents.agentpolis.simmodel.environment.model;

import cz.agents.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.agents.agentpolis.simulator.visualization.visio.entity.EntityPositionUtil;
import java.util.Comparator;
import javax.vecmath.Point2d;

/**
 *
 * @author F.I.D.O.
 * @param <E> entity type
 * @param <EP> entityPositionUtil type
 */
public class NearestEntityComparator<E extends AgentPolisEntity, EP extends EntityPositionUtil> 
        implements Comparator<E>{
	
	protected final Point2d from;
	
	protected final EP entityPositionUtil;

	
	public NearestEntityComparator(EP entityPositionUtil, Point2d from) {
		this.from = from;
		this.entityPositionUtil = entityPositionUtil;
	}
	
	

	@Override
	public int compare(AgentPolisEntity e1, AgentPolisEntity e2) {
		return Double.compare(entityPositionUtil.getEntityPosition(e1).distance(from), 
				entityPositionUtil.getEntityPosition(e2).distance(from));
	}
	
}