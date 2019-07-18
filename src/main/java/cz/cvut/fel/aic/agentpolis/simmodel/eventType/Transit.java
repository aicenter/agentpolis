/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.agentpolis.simmodel.eventType;

import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import java.math.BigInteger;

/**
 *
 * @author fido
 * @param <A> DRiver agent
 */
public class Transit<A extends Agent & Driver> {
	private final long time;
	
	private final BigInteger staticEdgeId;
	
	private final int tripId;
	
	private final A agent;

	public long getTime() {
		return time;
	}

	public BigInteger getId() {
		return staticEdgeId;
	}

	public int getTripId() {
		return tripId;
	}

	public A getAgent() {
		return agent;
	}


	
	

	
	
	public Transit(long time, BigInteger staticEdgeId, int tripId, A agent) {
		this.time = time;
		this.staticEdgeId = staticEdgeId;
		this.tripId = tripId;
		this.agent = agent;
	}


}
