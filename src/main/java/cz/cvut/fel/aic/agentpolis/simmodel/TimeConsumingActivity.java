/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandler;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;

/**
 *
 * @author fido
 * @param <A> Agent type
 */
public abstract class TimeConsumingActivity<A extends Agent> extends Activity<A>{
	protected long delay;
	
	public TimeConsumingActivity(ActivityInitializer activityInitializer, A agent) {
		super(activityInitializer, agent);
	}

	@Override
	void runActityLogic() {
		this.delay = performPreDelayActions();
		if(!failed()){
			getEventProcessor().addEvent(new EventHandler() {

					@Override
					public void handleEvent(Event event) {
						performPostDelayActions();
					}

					@Override
					public EventProcessor getEventProcessor() {
						return null;
					}

			}, delay);
		}
	}

	protected abstract long performPreDelayActions();

	private void performPostDelayActions() {
		super.runActityLogic();
	}
	
	
	
	
}
