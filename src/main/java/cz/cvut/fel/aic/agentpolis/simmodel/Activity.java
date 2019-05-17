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
package cz.cvut.fel.aic.agentpolis.simmodel;

import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.typed.AliteEntity;
import org.slf4j.LoggerFactory;

/**
 * This class represent agent's activity. 
 * @author F.I.D.O.
 * @param <A> Agent type
 */
public abstract class Activity<A extends Agent> extends AliteEntity{
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Activity.class);
	
	/**
	 * Agent who performs this activity.
	 */
	protected final A agent;
	
	/**
	 * Parent activity.
	 */
	private Activity parrentActivity;
	
	/**
	 * Child activity.
	 */
	private Activity childActivity;
	
	private boolean failed;
	
	
	

	public boolean failed() {
		return failed;
	}
	
	
	
	/**
	 * Agent getter.
	 * @return Returns agent who performs this activity.
	 */
	public final A getAgent() {
		return agent;
	}

	Activity getParrentActivity() {
		return parrentActivity;
	}
	
	


	/**
	 * Standard constructor
	 * @param activityInitializer
	 * @param agent Agent who performs this activity.
	 */
	public Activity(ActivityInitializer activityInitializer, A agent) {
		this.agent = agent;
		init(activityInitializer.getSimulation());
		failed = false;
	}


	
	
	@Override
	public void handleEvent(Event event) {
		if(parrentActivity != null){
			parrentActivity.handleEvent(event); 
		}
	}
	
	
	/**
	 * Action logic.
	 */
	protected abstract void performAction();

	
	/**
	 * This method finishes activity and calls parent activity if it exists or agent if it is not. If your activity 
	 * should be finished, this is the rigt place.
	 */
	protected final void finish(){
		if(parrentActivity != null){
			agent.currentActivity = parrentActivity;
			parrentActivity.onChildActivityFinish(this);
			parrentActivity.childActivity = null;
		}
		else{
			agent.currentActivity = null;
			agent.onActivityFinish(this);
		}
	}
	
	/**
	 * If something gets wrong in your activity that cannot be fixed, it should call this method.
	 * @param reason Reason of the failure
	 */
	protected final void fail(String reason){
		failed = true;
		agent.onActionFailed(this, reason);
	}
	
	/**
	 * This method is called when child activity is finished.
	 * @param activity Child activity.
	 */
	protected void onChildActivityFinish(Activity activity) {
		LOGGER.trace("{}: Child action finished: {}", this.getClass(), childActivity);
	}
	
	/**
	 * Runs child activity.
	 * @param childActivity Child activity. 
	 */
	protected final void runChildActivity(Activity childActivity){
		this.childActivity = childActivity;
		childActivity.parrentActivity = this;
		this.childActivity.run();
	}
	
	
	/**
	 * Main activity method. This method is called automaticaly by framework.
	 */   
	public final void run(){
		LOGGER.trace("{}: run() START", this.getClass());
		agent.currentActivity = this;
		runActityLogic();   
		LOGGER.trace("{}: run() END", this.getClass());
	}

	void runActityLogic() {
		performAction();
	}
	
	public void processMessage(Message message){
		if(parrentActivity != null){
			parrentActivity.processMessage(message);
		}
	}
}
