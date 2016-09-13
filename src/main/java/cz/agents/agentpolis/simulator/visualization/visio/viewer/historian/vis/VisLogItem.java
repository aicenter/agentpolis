/**
 * 
 */
package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.vis;

import java.awt.Rectangle;

import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItem;

/**
 * Copy/paste with some modification from AgentC
 * @author Zbynek Moler
 *
 */
/**
 * Standard {@link java.awt.Rectangle} with tick in it.
 * 
 * @author Ondrej Vanek
 * 
 */
public class VisLogItem extends Rectangle {

	private static final long serialVersionUID = 694629537054581235L;
	
	private final ViewLogItem event;

	public VisLogItem(int x, int y, int width, int height, int tick, ViewLogItem event) {
		super(x, y, width, height);
		this.event  = event;
	}

	/**
	 * @return the event
	 */
	public ViewLogItem getEvent() {
		return event;
	}
	
	

}
