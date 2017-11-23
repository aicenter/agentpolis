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
